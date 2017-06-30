# xxl-pm
项目管理软件

Activiti流程驱动，需求管理；

***
1、底层框架：spring3.2.9(mvc) + mybatis3.2.7 + mysql5.5
2、登陆系统：cookie = sameKey:cookieIdentity; cache = sameKey + userName : loginIdentity
(用户登陆信息,缓存key (cookie=sameKey:cacheIdentity; cache:sameKey+userName:loginIdentity))
3、activiti实现PM项目管理项目


-----------------------------------------------
线上线下,差异配置文件：
	1、jdbc.properties							：数据库配置
	2、freemarker.variables.properties			：freemarker全局变量(跟地址配置)

-----------------------------------------------
JS插件引用：
	001：001：bootstrap3.3.4:未做修改