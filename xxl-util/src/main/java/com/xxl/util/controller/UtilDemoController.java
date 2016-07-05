package com.xxl.util.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.util.core.util.CookieUtil;
import com.xxl.util.core.util.DateFormatUtil;
import com.xxl.util.core.util.HtmlTemplateUtil;
import com.xxl.util.core.util.HttpClientUtil;
import com.xxl.util.core.util.HttpSessionUtil;
import com.xxl.util.core.util.IdCardUtil;
import com.xxl.util.core.util.JacksonUtil;
import com.xxl.util.core.util.KaptchaUtil;
import com.xxl.util.core.util.MailUtil;
import com.xxl.util.core.util.Md5Util;
import com.xxl.util.core.util.PropInjectUtil;
import com.xxl.util.core.util.PropertiesUtil;
import com.xxl.util.core.util.RandomUtil;
import com.xxl.util.core.util.RegexUtil;
import com.xxl.util.core.util.ServletContextUtil;
import com.xxl.util.core.util.SpringContentUtil;
import com.xxl.util.core.util.TableInjectUtil;
import com.xxl.util.core.util.URLEncoderUtil;
import com.xxl.util.core.util.WebPathUtil;
import com.xxl.util.core.util.XMemcachedUtil;

/**
 * UTIL Demo
 * @author xuxueli 2016-6-29 18:59:43
 */
@Controller
public class UtilDemoController {
	protected static Logger logger = LoggerFactory.getLogger(UtilDemoController.class);
	
	/**
	 * <pre>
	1、HtmlTemplateUtil.java
		功能简介：
			根据Ftl生成字符串；
			根据Ftl生成Html文件（网站静态化）；
			Ftl支持使用静态类方法；
	
		使用步骤
			1、maven依赖
			<dependency>
				<groupId>org.freemarker</groupId>
				<artifactId>freemarker</artifactId>
				<version>2.3.20</version>
			</dependency>
			2、引入HtmlTemplateUtil.java文件
			3、配置FreeMarkerConfigurer（推荐托管在Spring容器，和复用SpringMVC同一套配置），见applicationcontext-base.xml文件配置
			<bean id="freemarkerConfig" class="org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer">
				<property name="freemarkerSettings">
					<bean class="org.springframework.beans.factory.config.PropertiesFactoryBean">
						<property name="location" value="classpath:freemarker.properties" />
					</bean>
				</property>
				<property name="templateLoaderPath" value="/WEB-INF/template/" />
				<property name="freemarkerVariables">
					<bean
						class="org.springframework.beans.factory.config.PropertiesFactoryBean">
						<property name="location" value="classpath:freemarker.variables.properties" />
					</bean>
				</property>
			</bean>
	 * </pre>
	 */
	@RequestMapping("/HtmlTemplateUtil")
	@ResponseBody
	public String HtmlTemplateUtil(){
		// generate String
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("WebPathUtil", HtmlTemplateUtil.generateStaticModel(WebPathUtil.class.getName()));
		
		String result = HtmlTemplateUtil.generateString(params, "freemarker-test.ftl");
		
		// generate Html File
		HtmlTemplateUtil.generateFile(params, "freemarker-test.ftl", "freemarker-test.html");
		
		return result;
	}
	
	/**
	 * <pre>
	2、HttpClientUtil.java
		功能简介：Http客户端
		使用步骤
			1、maven依赖
			<dependency>
				<groupId>org.apache.httpcomponents</groupId>
				<artifactId>httpclient</artifactId>
				<version>4.3.6</version>
			</dependency>
			2、引入HttpClientUtil.java文件
	 * </pre>
	 */
	@RequestMapping("/HttpClientUtil")
	@ResponseBody
	public String HttpClientUtil(){
		String result = HttpClientUtil.post("https://www.hao123.com/", null);
		return result;
	}
	
	/**
	 * <pre>
	3、JacksonUtil
		功能简介：Json类库;
		使用步骤
			1、maven依赖
			<dependency>
				<groupId>org.codehaus.jackson</groupId>
				<artifactId>jackson-mapper-asl</artifactId>
				<version>1.9.13</version>
			</dependency>
			2、引入JacksonUtil.java文件
	 * </pre>
	 */
	@RequestMapping("/JacksonUtil")
	@ResponseBody
	public String JacksonUtil(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("aaa", "111");
		map.put("bbb", "222");
		
		String result = JacksonUtil.writeValueAsString(map);
		return result;
	}
	
	/**
	 * <pre>
	4、PropertiesUtil.java
		功能简介：properties配置操作工具；
		使用步骤
			1、引入PropertiesUtil.java文件
	 * </pre>
	 */
	@RequestMapping("/PropertiesUtil")
	@ResponseBody
	public String PropertiesUtil(){
		Properties prop = PropertiesUtil.loadProperties("config.properties");
		
		String result = PropertiesUtil.getString(prop, "name");
		return result;
	}
	
	/**
	 * <pre>
	5、slf4j
		功能简介：日志组件
		使用步骤
			1、maven依赖
			<dependency>
				<groupId>org.slf4j</groupId>
				<artifactId>slf4j-log4j12</artifactId>
				<version>1.7.5</version>
			</dependency>
			<dependency>
				<groupId>org.slf4j</groupId>
				<artifactId>slf4j-api</artifactId>
				<version>1.7.5</version>
			</dependency>
			<dependency>
				<groupId>log4j</groupId>
				<artifactId>log4j</artifactId>
				<version>1.2.17</version>
			</dependency>
			2、配置log4j.properties或者log4j.xml文件；
			3、用法
			protected static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
			logger.error("close {} error!", propertyFileName);
	 * </pre>
	 */
	@RequestMapping("/Slf4j")
	@ResponseBody
	public String Slf4j(){
		logger.info("log info, param:{}", 999);
		return "slf4j";
	}
	
	/**
	 * <pre>
	6、SpringContentUtil.java
		功能简介：加载Spring容器中Bean的工具；
		使用步骤
			1、依赖Spring容器；
			2、引入SpringContentUtil.java文件
			3、需要实例化该Bean；见applicationcontext-util.xml配置；
				<bean id="springContentUtil" class="com.xxl.util.core.util.SpringContentUtil" />
			4、使用方法：
				SpringContentUtil.getBeanByName("freemarkerConfig");
	 * </pre>
	 */
	@RequestMapping("/SpringContentUtil")
	@ResponseBody
	public Object SpringContentUtil(){
		return SpringContentUtil.getBeanByName("freemarkerConfig");
	}
	
	/**
	 * <pre>
	7、WebPathUtil.java
		功能简介：
			获取Web项目class根目录；
			获取Web项目的Web根目录；
		使用步骤
			1、引入WebPathUtil.java文件
			2、如何使用：直接调用即可，如 “File htmlFile = new File(WebPathUtil.webPath() + filePathName);”生成Web目录下文件；
	 * </pre>
	 */
	@RequestMapping("/WebPathUtil")
	@ResponseBody
	public String WebPathUtil(){
		return WebPathUtil.webPath();
	}
	
	/**
	 * <pre>
	8、PropInjectUtil.java
		功能简介：
			自动将“prop文件”中“键值对类型配置”注入到静态属性中，支持配置刷新；反射方式实现
		使用步骤
			1、引入PropInjectUtil.java文件
			2、如何使用：直接调用即可，如 “PropInjectUtil.name”
	 * </pre>
	 */
	@RequestMapping("/PropInjectUtil")
	@ResponseBody
	public String PropInjectUtil(){
		return PropInjectUtil.name;
	}
	
	/**
	 * <pre>
	9、TableInjectUtil.java
		功能简介：
			自动将“文本文件”中“表格类型配置”注入到静态属性中，支持配置刷新；反射方式实现
		使用步骤
			1、引入TableInjectUtil.java文件
			2、如何使用：直接调用即可，如 “TableInjectUtil.table.size()”
	 * </pre>
	 */
	@RequestMapping("/TableInjectUtil")
	@ResponseBody
	public int TableInjectUtil(){
		return TableInjectUtil.table.size();
	}
	
	/**
	 * <pre>
	10、IdCardUtil.java
		功能简介：
			身份证，计算生日
		使用步骤
			1、引入IdCardUtil.java文件
			2、如何使用：直接调用即可，如 “IdCardUtil.getBirthDate(String idcard)”
	 * </pre>
	 */
	@RequestMapping("/IdCardUtil")
	@ResponseBody
	public String IdCardUtil(){
		return new SimpleDateFormat("yyyy-MM-dd").format(IdCardUtil.getBirthDate("412326200802201234"));
	}
	
	/**
	 * <pre>
	11、RegexUtil.java
		功能简介：
			Java正则表达式，校验工具类
		使用步骤
			1、引入RegexUtil.java文件
			2、如何使用：直接调用即可，如 “RegexUtil.isMobile("17701093170")”
	 * </pre>
	 */
	@RequestMapping("/RegexUtil")
	@ResponseBody
	public boolean RegexUtil(){
		return RegexUtil.isMobile("17701093170");
	}
	
	/**
	 * <pre>
	12、URLEncoderUtil.java
		功能简介：
			URL编码解码，Java工具类；对应js中的encodeURI和decodeURI；
		使用步骤
			1、引入URLEncoderUtil.java文件
			2、如何使用：直接调用即可，如 “RegexUtil.isMobile("17701093170")”
	 * </pre>
	 */
	@RequestMapping("/URLEncoderUtil")
	@ResponseBody
	public String URLEncoderUtil(){
		return URLEncoderUtil.encode("https://www.baidu.com/点评");
	}
	
	/**
	 * <pre>
	13、CookieUtil.java
		功能简介：
			cookie工具类
		使用步骤
			1、引入CookieUtil.java文件
			2、如何使用：直接调用即可，如 “RegexUtil.isMobile("17701093170")”
	 * </pre>
	 */
	@RequestMapping("/CookieUtil")
	@ResponseBody
	public String CookieUtil(HttpServletRequest request, HttpServletResponse response){
		CookieUtil.set(response, "test", "999", true);
		String temp = CookieUtil.getValue(request, "test");
		return temp;
	}
	
	/**
	 * <pre>
	14、RandomUtil.java
		功能简介：
			随机字符串生成，用户随机/短信验证码生成等
		使用步骤
			1、引入RandomUtil.java文件
			2、如何使用：直接调用即可，如 “RandomUtil.randomStr(RandomUtil.NUMBERS, 9);”
	 * </pre>
	 */
	@RequestMapping("/RandomUtil")
	@ResponseBody
	public String RandomUtil(){
		return RandomUtil.randomStr(RandomUtil.NUMBERS, 9);
	}
	
	/**
	 * <pre>
	15、DateFormatUtil.java
		功能简介：
			日期格式化，日期解析
		使用步骤
			1、引入DateFormatUtil.java文件
			2、如何使用：直接调用即可，如 “RandomUtil.randomStr(RandomUtil.NUMBERS, 9);”
	 * </pre>
	 */
	@RequestMapping("/DateFormatUtil")
	@ResponseBody
	public String DateFormatUtil(){
		try {
			return DateFormatUtil.formatDateTime(new Date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * <pre>
	16、HttpSessionUtil.java
		功能简介：
			HttpSession(session会话)工具类
		使用步骤
			1、引入HttpSessionUtil.java文件
			2、如何使用：直接调用即可，如 “HttpSessionUtil.get(session, "test")”
	 * </pre>
	 */
	@RequestMapping("/HttpSessionUtil")
	@ResponseBody
	public String HttpSessionUtil(HttpSession session){
		String temp = null;
		try {
			HttpSessionUtil.set(session, "test", "999");
			temp = (String) HttpSessionUtil.get(session, "test");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	/**
	 * <pre>
	17、ServletContextUtil.java
		功能简介：
			ServletContextUtil(Web全局)工具类
		使用步骤
			1、引入ServletContextUtil.java文件
			2、如何使用：直接调用即可，如 “ServletContextUtil.get(request.getSession().getServletContext(), "test")”
	 * </pre>
	 */
	@RequestMapping("/ServletContextUtil")
	@ResponseBody
	public String ServletContextUtil(HttpServletRequest request){
		ServletContextUtil.set(request.getSession().getServletContext(), "test", "666");
		return (String) ServletContextUtil.get(request.getSession().getServletContext(), "test");
	}
	
	/**
	 * <pre>
	18、Md5Util.java
		功能简介：
			Md5加密工具类
		使用步骤
			1、引入Md5Util.java文件
			2、如何使用：直接调用即可，如 “Md5Util.encrypt("test")”
	 * </pre>
	 */
	@RequestMapping("/Md5Util")
	@ResponseBody
	public String Md5Util(){
		return Md5Util.encrypt("test");
	}
	
	/**
	 * <pre>
	19、MailUtil.java
		功能简介：
			邮件发送工具类
		使用步骤
			1、maven依赖：
				<dependency>
					<groupId>javax.mail</groupId>
					<artifactId>mail</artifactId>
					<version>1.4.6</version>
				</dependency>
			2、配置JavaMailSenderImpl的BEAN（API方式可忽略）
				<bean id="javaMailSender" class="org.springframework.mail.javamail.JavaMailSenderImpl"  scope="singleton" >
					<property name="host" value="${mail.host}" />			<!-- SMTP发送邮件的服务器的IP和端口 -->
					<property name="port" value="${mail.port}" />
					<property name="username" value="${mail.username}" />	<!-- 登陆SMTP邮件发送服务器的用户名和密码 -->
					<property name="password" value="${mail.password}" />
					<property name="javaMailProperties">					<!-- 获得邮件会话属性,验证登录邮件服务器是否成功 -->
						<props>
							<prop key="mail.smtp.auth">true</prop>
							<prop key="prop">true</prop>
							<!-- <prop key="mail.smtp.timeout">25000</prop> -->
						</props>
					</property>
				</bean>
			3、引入MailUtil.java文件
			4、如何使用：直接调用即可，如 “MailUtil.sendMailSpring("931591021@qq.com", "测试邮件1", "Hi", false, null);”
	 * </pre>
	 */
	@RequestMapping("/MailUtil")
	@ResponseBody
	public boolean MailUtil(){
		MailUtil.sendMailSpring("931591021@qq.com", "测试邮件1", "Hi", false, null);
		return MailUtil.sendMail("931591021@qq.com", "测试邮件2", "Hi", false, null);
	}
	
	/**
	 * <pre>
	20、KaptchaUtil.java
		功能简介：
			Kaptcha验证码生成
		使用步骤
			1、maven依赖
				<dependency>
					<groupId>com.github.axet</groupId>
					<artifactId>kaptcha</artifactId>
					<version>0.0.9</version>
				</dependency>
			2、配置DefaultKaptcha的BEAN (参数配置可参考：http://www.cnblogs.com/louis80/p/5230507.html )
				<bean id="captchaProducer" class="com.google.code.kaptcha.impl.DefaultKaptcha">
					<property name="config">
						<bean class="com.google.code.kaptcha.util.Config">
							<constructor-arg type="java.util.Properties">
								<props>
									<prop key="kaptcha.textproducer.char.string">10</prop>	<!-- 生成验证码内容范围 -->
									<prop key="kaptcha.textproducer.char.length">1</prop>	<!-- 验证码个数 -->
									<prop key="kaptcha.textproducer.char.space">10</prop>	<!-- 验证码文本字符间距 -->
								</props>
							</constructor-arg>
						</bean>
					</property>
				</bean>
			3、引入KaptchaUtil.java文件
			4、生成验证码：controller中执行 “KaptchaUtil.generateImage(request, response);” 即可 (验证码默认缓存在session的某个key中) ；
			3、校验验证码：
				// 03：随机验证码拦截
				String randCodeCache = (String) HttpSessionUtil.get(session, Constants.KAPTCHA_SESSION_KEY);
				String randCodeParam = request.getParameter("randCode");
				if (StringUtils.isBlank(randCodeCache) || !StringUtils.equals(randCodeCache, randCodeParam)) {
					throw new WebException(ReturnCodeEnum.FAIL.code(), "验证码错误");
				}
	 * </pre>
	 * @throws Exception 
	 */
	@RequestMapping("/KaptchaUtil")
	public void KaptchaUtil(HttpServletRequest request, HttpServletResponse response) throws Exception{
		KaptchaUtil.generateImage(request, response);
	}
	
	/**
	 * <pre>
	21、XMemcachedUtil.java
		功能简介：
			Md5加密工具类
		使用步骤
			1、maven依赖
				<dependency>
					<groupId>com.googlecode.xmemcached</groupId>
					<artifactId>xmemcached</artifactId>
					<version>2.0.0</version>
				</dependency>
			2、配置memcached.properties中的，地址列表，和权重
				# distributed memcached client config :address="hostMain1:port,hostBack1:port hostMain2:port,hostBack2:port",	weights={1, 2}
				server.address=127.0.0.1:11211,127.0.0.1:11211 127.0.0.1:11211,127.0.0.1:11211
				server.weights=5,5
			3、引入XMemcachedUtil.java文件
			4、如何使用：直接调用即可，如 “Md5Util.encrypt("test")”
	 * </pre>
	 */
	@RequestMapping("/XMemcachedUtil")
	@ResponseBody
	public String XMemcachedUtil(){
		XMemcachedUtil.set("test", "666");
		return (String) XMemcachedUtil.get("test");
	}
	
}
