package com.xxl.util.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.xxl.util.core.skill.xml.model.User;
import com.xxl.util.core.skill.xml.util.XStreamUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xxl.util.core.skill.flowcontrol.ReturnT;
import com.xxl.util.core.skill.flowcontrol.WebException;
import com.xxl.util.core.skill.threadpool.ThreadPoolLinkedHelper;
import com.xxl.util.core.skill.threadpool.ThreadPoolQueueHelper;
import com.xxl.util.core.util.WebPathUtil;

/**
 * skill demo
 * @author xuxueli 2016-7-6 16:27:44
 */
@Controller
@RequestMapping("/skill")
public class SkillDemoController {
	private static Logger logger = LoggerFactory.getLogger(SkillDemoController.class);
	
	/**
	 * <pre>
	1、ThreadPoolQueueHelper.java/ThreadPoolLinkedHelper.java
		功能简介：
			生产消费者模型，FIFO队列，线程池，异步
		方式A：“Executors.newCachedThreadPool() + LinkedBlockingQueue”方式实现
			特点：启动时初始化指定数量Producer、Consumer线程；Producer负责push数据到queue中，Consumer负责从queue中pull数据并处理；
		方式B：“ThreadPoolExecutor”方式实现
			特点：启动时初始化ThreadPool(内部线程使用LinkedBlockingQueue维护)，以及Producer线程；Producer负责为每条数据在ThreadPool中创建新的线程，每个线程run方法即消费者逻辑方法；
	   </pre>
	 * @return
	 */
	@RequestMapping("/producerConsumer")
	@ResponseBody
	public String producerConsumer(){
		/*
		ThreadPoolQueueHelper.pushData("producerConsumer-" + System.currentTimeMillis());	
		ThreadPoolLinkedHelper.pushData("producerConsumer-" + System.currentTimeMillis());
		*/
		ThreadPoolQueueHelper.getInstance();	// 使用时，线程池需要主动触发；如实例化bean方式，或者listener方式；
		ThreadPoolLinkedHelper.getInstance();
		
		return "200";
	}
	
	/**
	 *<pre>
	 2、FileuploadUtil.java
	 	功能简介：
	 		文件上传工具，“commons-fileupload + spring mvc”方式实现
	 	使用步骤：
	 		1、maven依赖
				<dependency>
					<groupId>commons-fileupload</groupId>
					<artifactId>commons-fileupload</artifactId>
					<version>1.3.2</version>
				</dependency>
				<dependency>
					<groupId>commons-io</groupId>
					<artifactId>commons-io</artifactId>
					<version>2.2</version>
				</dependency>
			2、配置spring mvc的CommonsMultipartResolver配置
				<!-- file upload -->
				<bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
				    <property name="defaultEncoding" value="utf-8"/>
				</bean>
			3、查看示例：“com.xxl.util.controller.SkillDemoController.fileupload(MultipartFile[], HttpServletRequest)” + “fileuploadpage.html”
		注意点：
			1. 如果只是上传一个文件，则只需要MultipartFile类型接收文件即可，而且无需显式指定@RequestParam注解
			2. 如果想上传多个文件，那么这里就要用MultipartFile[]类型来接收文件，并且还要指定@RequestParam注解
			3. 并且上传多个文件时，前台表单中的所有<input type="file"/>的name都应该是imgFiles，否则参数里的imgFiles无法获取到所有上传的文件
	 *</pre>
	*/
	
	private static List<String> allowSuffix = Arrays.<String>asList("gif,jpg,jpeg,png,bmp".split(","));;
	
	@RequestMapping(value="/fileupload")
	@ResponseBody
	public Map<String, String> fileupload(@RequestParam MultipartFile[] imgFiles, HttpServletRequest request) {
		// 校验上传目录
		String realPath = WebPathUtil.webPath(request).concat("/uploadimg");
		File folder = new File(realPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		
		Map<String, String> uploadInfoMap = new HashMap<String, String>();
		for (MultipartFile img : imgFiles) {
			if (img.isEmpty()) {
				logger.warn("图片上传失败+1，图片为空");
				continue;
			}
			if (img.getSize() > 10000000) {
				logger.warn("图片上传失败+1，大小超过限制10M，name:{}， size:{}", img.getName(), img.getSize());
				continue;
			}
			// 文件后缀
			String suffix = img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf(".") + 1).toLowerCase();
			if (!(allowSuffix.contains(suffix))) {
				logger.warn("图片上传失败+1，图片后缀不合法，name={}，originalFilename={}", img.getName(), img.getOriginalFilename());
				continue;
			}
			
			logger.warn("------ image process start ------");
			logger.warn("size:{}, contentType:{}, name:{}, originalFilename:{}", 
					img.getSize(), img.getContentType(), img.getName(), img.getOriginalFilename());
			String newFileName = String.valueOf(System.currentTimeMillis()).concat(".").concat(suffix);
			uploadInfoMap.put(img.getOriginalFilename(), newFileName);
			try {
				// 不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
				FileUtils.copyInputStreamToFile(img.getInputStream(), new File(realPath, newFileName));
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return uploadInfoMap;
	}
	
	/**
	 * <pre>
	3、ReturnT.java/WebException.java/WebExceptionResolver.java
		功能简介：
			业务流程控制，两种方式：“ReturnT”和“WebException+异常解析器”方式；
		方式A：“ReturnT.java”方式
			特点：项目底层，封装ReturnT（code、msg、content为泛型），逻辑流程中返回“封装ReturnT”，controller中根据返回值
			使用步骤：
				1、引入ReturnT.java
				2、使用，Controller如 “@ResponseBody public ReturnT<String> flowControl()”；Service中如 “public ReturnT<Integer> articleMenuAdd(ArticleMenu menu);”；
		方式B：“WebException.java + WebExceptionResolver.java”方式实现
			特点：项目底层“异常机制”统一catch进行“自定义异常”处理，逻辑流程中抛出“自定义异常”，然后跳转统一error页或返回统一json返回值（WebException转json），这样在controller中不用写一堆的try/catch，甚至不需要定义和处理返回值，灵活方便；
			使用步骤：
				1、引入 “WebException.java + WebExceptionResolver.java”
				2、配置SpringMvc异常解析器：
					<bean id="exceptionResolver" class="com.xxl.util.core.skill.flowcontrol.WebExceptionResolver" />
				3、使用，业务中抛出该异常即可，如 “throw new WebException("流程控制");” 
	   </pre>
	 * @return
	 */
	@RequestMapping(value="/flowControl")
	@ResponseBody
	public ReturnT<String> flowControl() {
		if (new Random().nextBoolean()) {
			throw new WebException("流程控制");
		}
		return new ReturnT<String>("流程控制");
	}

	/**
	 * <pre>
	 3、XStreamUtil.java 或者 XmlDom4jImpl.java / XmlDomImpl.java / XmlSaxImpl.java
		 功能简介：
	 		XML解析生成: XStream方式, 功能完善; Dom4j/DOM/SAX方式, 比较原始,需要根据对象属性编码定制
		 方式A：“XStreamUtil.java”方式
			特点：功能完善
		 	使用步骤：
				1、依赖
					<dependency>
						 <groupId>com.thoughtworks.xstream</groupId>
						 <artifactId>xstream</artifactId>
						 <version>1.4.7</version>
					 </dependency>
				2、引入 XStreamUtil.java
				3、使用，XStreamUtil.java 中静态方法调用即可;
		 方式B：XmlDom4jImpl.java / XmlDomImpl.java / XmlSaxImpl.java
				特点：比较原始,需要根据对象属性编码定制
			 使用步骤：
				1、引入依赖
					<dependency>
						<groupId>dom4j</groupId>
						<artifactId>dom4j</artifactId>
						<version>1.6</version>
				 	</dependency>

				 2、引入 XmlDom4jImpl.java / XmlDomImpl.java / XmlSaxImpl.java
				 3、使用，XmlDom4jImpl.java 中方法调用即可;
	 </pre>
	 * @return
	 */
	@RequestMapping(value="/xmlParse")
	@ResponseBody
	public ReturnT<String> xmlParse() {
		List<User> userList = new ArrayList<User>();
		userList.add(new User(2, "jack", 18, "女"));

		String xml = XStreamUtil.bean2xml(userList);
		return new ReturnT<String>(xml);
	}
	
}
