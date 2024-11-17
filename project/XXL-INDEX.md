### 资源汇总

---
> src
- https://github.com/xuxueli
- http://git.oschina.net/xuxueli0323 （backup）

> notebook
- https://github.com/xuxueli/xuxueli.github.io#notebook
- http://my.oschina.net/xuxueli/blog （backup）
- http://www.cnblogs.com/xuxueli （backup）


### 开源产品汇总

序号 | 项目             | 协议         | 特征        |  特征
----|----------------|------------|-----------|--- 
1   | xxl-job        | GPL        | 作业调度（产品）  |  轻量级、分布式(jdk17)
2   | xxl-conf       | GPL        | 配置管理（产品）  |  妙级推送、一致性、权限、安全(灰度、可观测)     
3   | xxl-rpc        | Apache     | 服务通讯（框架）  |  轻量级、高性能(重构)       
4   | xxl-mq         | GPL【TODO】 | 消息队列（框架）  |  轻量级、可追溯、延时、重试(长轮训)          
5   | xxl-cache      | GPL【TODO】 | 多级缓存（框架）  |  两级缓存、redis+ caffeine(全局热点)
6   | xxl-sso        | GPL        | 单点登录（产品）  |  单点登录、登录、鉴权(sdk化)
7   | xxl-api        | GPL        | API管理（产品）  |   API管理、Mock、测试、(导入导出)
8   | xxl-crawler    | Apache     | 爬虫框架 （框架） |  爬虫                 
9   | xxl-tool       | Apache     | 工具包 （框架）   |  工具包，零依赖                
10  | xxl-boot       | GPL        | 脚手架（产品）    |  快速开发框架，(前后端分离)             
11  | pages          | GPL        | 官网            |  官网、博客 blog/notebook、文档 doc；
12  | xxl-incubator  | GPL        | 孵化器          |   孵化器，脑暴             

### etc 
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
        
       
### 开发环境

- OS：mac + homebrew 
- Java：jdk + maven + git + idea
- DB：mysql + idea-database
- Linux：docker + terminal
- Etc：sublime
- Other：Chrome + 百度云 + office
