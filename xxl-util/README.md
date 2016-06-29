# xxl-util
Standby skill points

### 1、HtmlTemplateUtil.java
***
##### 功能简介：
	根据Ftl生成字符串；
	根据Ftl生成Html文件（网站静态化）；
	Ftl支持使用静态类方法；

##### 使用步骤
> 1、maven依赖
```
<dependency>
	<groupId>org.freemarker</groupId>
	<artifactId>freemarker</artifactId>
	<version>2.3.20</version>
</dependency>
```
> 2、引入HtmlTemplateUtil.java文件
> 3、配置FreeMarkerConfigurer（推荐托管在Spring容器，和复用SpringMVC同一套配置），见applicationcontext-base.xml文件配置
```
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
```
> 4、使用示例：见：com.xxl.util.controller.IndexController.HtmlTemplateUtil()；


### 2、HttpUtil.java
***
##### 功能简介：	Http客户端；
##### 使用步骤
> 1、maven依赖
```
<dependency>
	<groupId>org.apache.httpcomponents</groupId>
	<artifactId>httpclient</artifactId>
	<version>4.3.6</version>
</dependency>
```
> 2、引入HttpUtil.java文件
> 3、使用示例：见：com.xxl.util.controller.IndexController.HttpClientUtil()；

### 3、JacksonUtil
##### 功能简介：	Json类库;
##### 使用步骤
> 1、maven依赖
```
<dependency>
	<groupId>org.codehaus.jackson</groupId>
	<artifactId>jackson-mapper-asl</artifactId>
	<version>1.9.13</version>
</dependency>
```
> 2、引入JacksonUtil.java文件
> 3、使用示例：见：com.xxl.util.controller.IndexController.JacksonUtil()；

### 4、PropertiesUtil.java
##### 功能简介：properties配置操作工具；
##### 使用步骤
> 1、引入PropertiesUtil.java文件
> 2、使用示例：见：com.xxl.util.controller.IndexController.PropertiesUtil()；
> 3、启动时自动初始化Configuration类（未完待续）

### 5、slf4j
##### 功能简介：日志组件 
##### 使用步骤
> 1、maven依赖
```
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
```
> 2、配置log4j.properties或者log4j.xml文件；
> 3、用法
```
protected static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
logger.error("close {} error!", propertyFileName);
```


### 6、SpringContentUtil.java
##### 功能简介：加载Spring容器中Bean的工具；
##### 使用步骤
> 1、依赖Spring容器；
> 2、引入SpringContentUtil.java文件
> 3、需要实例化该Bean；见applicationcontext-util.xml配置；
```
<bean id="springContentUtil" class="com.xxl.util.core.util.SpringContentUtil" />
```
> 4、使用方法：
```
SpringContentUtil.getBeanByName("freemarkerConfig");
```

### 7、WebPathUtil.java
##### 功能简介：
	获取Web项目class根目录；
	获取Web项目的Web根目录；
##### 使用步骤
> 1、引入WebPathUtil.java文件
> 2、如何使用：直接调用即可，如 “File htmlFile = new File(WebPathUtil.webPath() + filePathName);”生成Web目录下文件；









