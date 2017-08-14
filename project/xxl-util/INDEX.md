# [xxl-util](https://github.com/xuxueli/xxl-incubator/tree/master/xxl-util)
Standby skill points


### 工具类（com.xxl.util.core.util目录）
***
* 1、HtmlTemplateUtil.java (Freemarker生成页面)
> 详细：见：com.xxl.util.controller.UtilDemoController.HtmlTemplateUtil()

* 2、HttpClientUtil.java		(Http请求工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.HttpClientUtil()

* 3、JacksonUtil		(Json工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.JacksonUtil()

* 4、PropertiesUtil.java		(配置加载工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.PropertiesUtil()

*启动时自动初始化Configuration类（未完待续）*

* 5、Slf4j	(介绍slf4j)
> 详细：见：com.xxl.util.controller.UtilDemoController.Slf4j()

* 6、SpringContentAwareUtil.java/SpringContentListenerUtil.java/SpringContentRequestUtil.java		(三种方式实现，加载Spring容器中Bean的工具；)
> 详细：见：com.xxl.util.controller.UtilDemoController.SpringContentUtil()

* 7、WebPathUtil.java	(获取Web和Class根目录路径工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.WebPathUtil()

* 8、PropInjectUtil.java		(自动将“prop文件”中“键值对类型配置”注入到静态属性中，支持配置刷新；反射方式实现)
> 详细：见：com.xxl.util.controller.UtilDemoController.PropInjectUtil()

* 9、TableInjectUtil.java	(自动将“文本文件”中“表格类型配置”注入到静态属性中，支持配置刷新；反射方式实现)
> 详细：见：com.xxl.util.controller.UtilDemoController.TableInjectUtil()

* 10、IdCardUtil.java	(身份证，计算生日)
> 详细：见：com.xxl.util.controller.UtilDemoController.IdCardUtil()

* 11、RegexUtil.java	(Java正则表达式，校验工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.RegexUtil()

* 12、URLEncoderUtil.java	(URL编码解码，Java工具类；对应js中的encodeURI和decodeURI；)
> 详细：见：com.xxl.util.controller.UtilDemoController.URLEncoderUtil()

* 13、CookieUtil.java	(cookie工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.CookieUtil()

* 14、RandomUtil.java	(随机字符串生成，用户随机/短信验证码生成等)
> 详细：见：com.xxl.util.controller.UtilDemoController.RandomUtil()

* 15、DateFormatUtil.java	(日期格式化，日期解析)
> 详细：见：com.xxl.util.controller.UtilDemoController.DateFormatUtil()

* 16、HttpSessionUtil.java	(HttpSession(session会话)工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.HttpSessionUtil()

* 17、ServletContextUtil.java	(ServletContextUtil(Web全局)工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.ServletContextUtil()

* 18、Md5Util.java	(Md5加密工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.Md5Util()

* 19、MailUtil.java	(邮件发送工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.MailUtil()

* 20、KaptchaUtil	(Kaptcha验证码生成)
> 详细：见：com.xxl.util.controller.UtilDemoController.KaptchaUtil()

* 21、XMemcachedUtil.java/SpyMemcachedUtil.java/MemcachedJavaClientUtil.java、JedisUtil.java	(memcached分布式缓存的客户端工具,三种实现方式; redis分布式缓存客户端jedis工具)
> 详细：见：com.xxl.util.controller.UtilDemoController.CacheUtil()

* 22、LoginUtil.java		(分布式，登陆验证器，cookie + memcached实现)
> 详细：见：com.xxl.util.controller.UtilDemoController.LoginUtil()

* 23、ZXingUtil.java		(二维码生成)
> 详细：见：com.xxl.util.controller.UtilDemoController.ZXingUtil()

* 24、IPSeeker.java		(IP地址解析成地址，利用纯真IP数据库)
> 详细：见：com.xxl.util.controller.UtilDemoController.IPSeeker()

- 25、PoiUtil.java	(操作Microsoft Office文档的工具类，支持Excel、Word和PPT，其中Excel尤其常见)
> 详细：见：com.xxl.util.controller.UtilDemoController.PoiUtil()

- 26、Jsonp接口开发  (方式1：Jackson之JSONPObject方式; 方式2：SpringMVC4之MappingJacksonValue方式)
> 详细：见：com.xxl.util.controller.UtilDemoController.jsonp()

- 27、MongoDBUtil.java  (MongoDB工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.mongodb()

- 28、ZookeeperUtil.java  (Zookeeper工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.ZookeeperUtil()

- 29、com.xxl.util.core.util.IpUtil.java  (IP工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.IpUtil()

- 30、com.xxl.util.core.util.ThreadLocalParamMapUtil.java  (ThreadLocal工具类)
> 详细：见：com.xxl.util.controller.UtilDemoController.ThreadLocalParamMapUtil()

- 31、com.xxl.util.core.util.ScriptUtil.java  (Java调用shell/python脚本工具类)

- 32、com.xxl.util.core.util.PinyinUtil.java  (汉语转拼音工具类)

- 33、com.xxl.util.core.util.MvcUtil （springmvc工具类，静态类支持等）

- 34、webapp/static/js/navigator.check.1.js （操作平台校验：PC、Android、IOS等）

- 35、webapp/static/js/requestParam.js （从URL中获取get参数）

- 36、com.xxl.util.core.util.ApiInvokeUtil （对象方式请求API接口，配合 "@RequestBody"）

- 37、com.xxl.util.core.util.DBUtil （DB操作工具类）

- 38、com.xxl.util.core.util.AccessLimitUtil （令牌桶示例Util）


### 技能点（com.xxl.util.core.skill目录）
- 1、ThreadPoolQueueHelper.java/ThreadPoolLinkedHelper.java	(两种方式实现：生产消费者模型，FIFO队列，线程池，异步)
> 详细：见：com.xxl.util.controller.SkillDemoController.producerConsumer()

- 2、commons-fileupload		(文件上传工具，“commons-fileupload + spring mvc”方式实现)
> 详细：见：com.xxl.util.controller.SkillDemoController.fileupload(MultipartFile[], HttpServletRequest)

- 3、ReturnT/WebException		(业务流程控制，两种方式：“ReturnT”和“WebException+异常解析器”方式；)
> 详细：见：com.xxl.util.controller.SkillDemoController.flowControl()

- 4、XStreamUtil.java/"Dom4j/DOM/SAX"     (XML解析生成: XStream方式, 功能完善; Dom4j/DOM/SAX方式, 比较原始,需要根据对象属性编码定制)
> 详细：见：com.xxl.util.controller.SkillDemoController.xmlParse()

- 5、JsoupUtil.java/HtmlParserUtil.java    (页面爬虫工具)
> 详细：见：com.xxl.util.controller.SkillDemoController.crawler()

- 6、JacksonSerializer.java/ProtostuffSerializer.java/HessianSerializer.java    (序列化工具(Object-字节数组): Jackson、Protostuff、Hessian等)
> 详细：见：com.xxl.util.controller.SkillDemoController.serializer()

- 7、ddos.html  (JS实现简易版本DDOS)
> 详细：见：/xxl-util/src/main/webapp/skill/ddos.html

- 8、截屏Java实现
> 详细：见：com.xxl.util.core.skill.screencapture.ScreenCaptureUtil

- 9、UEditor 富文本编辑器,支持图片或文件上传
> 详细：见：/xxl-util/src/main/webapp/skill/ueditor.html

- 10、Log4j日志AppenderSkeleton实现
> 详细：见：com.xxl.util.core.skill.logappeng.XxlJobFileAppender