## 《xxl-web》

## 简介
Java Web Framework.

## 特性
- 1、MVC：Page、JSON等Web请求；
- 2、API功能：每个API接口只需要开发相应的Handler即可；
- 3、JSONP兼容：JSON接口同时支持JSONP方式调用；
- 4、Console：支持查看可提供服务的页面和API接口列表；

## 快速入门

可参考示例项目 "xxl-web-example" 。

##### 1、maven依赖

```
<!-- xxl-web-core -->
<dependency>
    <groupId>com.xxl</groupId>
    <artifactId>xxl-web-core</artifactId>
    <version>${project.parent.version}</version>
</dependency>
```

##### 2、web.xml 配置统一路由

```
<!-- xxl-web start -->
<servlet>
    <servlet-name>xxlapi</servlet-name>
    <servlet-class>com.xxl.web.core.dispatch.XxlWebDispatchServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>xxlapi</servlet-name>
    <url-pattern>*.xxlapi</url-pattern>
</servlet-mapping>
<!-- xxl-web stop -->
```

##### 3、xxl-web 容器初始化

可参考示例项目 "apihome-client-example" 配置文件 "applicationcontext-apihome.xml"；

```
<!-- init xxl-web handler factory -->
<bean class="com.xxl.web.core.handler.XxlWebHandlerFactory" />

<!-- scan xxl-web handler -->
<context:component-scan base-package="com.xxl.web.example.handler" />
```

至此，配置全部Finish。

##### 4、Hello World. (开发第一个API)

xxl-web中每一个接口对应三个文件：Request + Handler + Response。例如示例项目中：

    DemoRequest: API接口请求参数对象，继承统一父类 "XxlWebRequest" 。前端传递的JSON数据将会自动反序列化生成请求对象。
    DemoResponse：API接口响应数据，通常结合 "JsonResponse" 使用。例如 "new JsonResponse<DemoResponse>(demoResponse);"，接口响应时将会整体序列化为JSON数据返回。
    DemoHandler：API接口业务处理器，继承统一父类 "XxlWebHandler" ，通过注解@ApiHandlerMapping绑定API路由。通常绑定指定参数对象，接收 "XxlWebRequest" 进行业务处理，返回Request对象。

## TODO
- 定位确定：轻量级java web框架；
    - web核心：server、mvc、ioc、aop、cookie；
    - resource：conf...
    - static（STATIC）：css、js、html
    - api（JSON）：json-native
    - render（PAGE）：ftl
    

