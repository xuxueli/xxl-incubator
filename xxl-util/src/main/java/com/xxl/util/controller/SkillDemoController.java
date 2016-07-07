package com.xxl.util.controller;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
			3、查看示例：“com.xxl.util.controller.SkillDemoController.fileupload(MultipartFile[], HttpServletRequest)”
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
			} else {
				logger.warn("------ image process start ------");
				logger.warn("size:{}, contentType:{}, name:{}, originalFilename:{}", 
						img.getSize(), img.getContentType(), img.getName(), img.getOriginalFilename());
				String newFileName = String.valueOf(System.currentTimeMillis()).concat(".").concat(suffix);
				uploadInfoMap.put(img.getName(), newFileName);
				try {
					// 不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉
					FileUtils.copyInputStreamToFile(img.getInputStream(), new File(realPath, newFileName));
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}

		return uploadInfoMap;
	}
	
}
