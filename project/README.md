### 资源汇总

---
> src
- https://github.com/xuxueli
- http://git.oschina.net/xuxueli0323 （backup）

> notebook
- https://github.com/xuxueli/xuxueli.github.io 
- http://my.oschina.net/xuxueli/blog （backup）
- http://www.cnblogs.com/xuxueli （backup）


### 开源产品

##### 平台

序号| 项目            | 协议              | 特征              |  特征
----|---------------|-----------------|-----------------|-------------- 
1   | xxl-job       | GPL             | 任务调度平台          |    轻量级、分布式、多语言、多模式（GLUE） (jdk17)
2   | xxl-mq        | GPL             | 延时消息中心          |    轻量级、轨迹追溯、延时、实时(长轮训)、分级、重试、多模式（GLUE）                        【TODO】
3   | xxl-conf      | GPL             | 配置/注册中心         |    妙级推送、一致性、权限（appkey维度）、安全(灰度、可观测)；分布式服务中心(注册&配置中心)    【ING】
4   | xxl-sso       | GPL             | 单点登录框架          |    基础登录、鉴权(sdk化)、单点登录                                                    【TODO】
5   | xxl-api       | GPL             | API管理平台         |     API管理、权限隔离、Mock Server、自动化测试、swagger导入导出
6   | xxl-boot      | GPL             | 中后台脚手架          |    快速开发框架、单体式、前后端分离

##### 库

序号 | 项目            | 协议              | 特征                   |  特征
----|---------------|-----------------|----------------------|--------------
1   | xxl-rpc       | Apache          | RPC框架                |  轻量级、高性能、全注解、运营（手动/自动、服务/节点禁用）
2   | xxl-crawler   | Apache          | 爬虫框架                 |  爬虫
3   | xxl-tool      | Apache          | 工具包                  |  工具包，零依赖
4   | xxl-cache     | GPL【整合】         | 缓存管理平台               |  主动清理，内存优化；二级缓存（caffeine+redis），双写；热点探测；                 【TODO】


##### 其他

序号 | 项目             | 协议             | 特征      |  特征
----|----------------|----------------|---------|---
1  | xuxuelipage    | GPL            | 官网          |  官网、博客 blog/notebook、文档 doc；
2  | xxl-incubator  | GPL            | 孵化器        |   孵化器，脑暴


### 孵化产品
```
xxl-incubator
    - /project
        - level-1：成品
            - xxl-apm
            - xxl-code-generator    : xxl-code-generator
            - xxl-glue
            - xxl-hex
            - xxl-permission        : 权限管理系统、项目脚手架，easyui
            - xxl-web
            - xxl-cache             ：缓存管理
        - level-2：demo级别
            - xxl-app
            - xxl-search
            - xxl-seckill 
            - xxl-util
        - 规划
            - xxl-url-shortener：url shortener；url rule, case when 'app/holiday/date/os/cityid/usertype/param' then 'real-url'
            - xxl-dynamic-page：可见即所得，拖拽组件、填写数据，立即生成生产环境页面地址；组件打通前后端，全流程；https://www.cnblogs.com/zhuanzhuanfe/p/10500786.html
    - /tmp : demo项目
        - ws ：微信小程序
```

### 开发环境
```
- OS：mac + homebrew 
- Java：jdk + maven + git + idea
- DB：mysql + idea-database
- Linux：docker + terminal
- Etc：sublime
- Other：Chrome + 百度云 + office
```
