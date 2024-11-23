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

序号 | 项目             | 协议                    | 特征      |  特征
----|----------------|-----------------------|---------|--- 
1   | xxl-job        | GPL                   | 任务调度平台  |  轻量级、批量、分布式(jdk17)
2   | xxl-mq         | GPL                   | 分布式消息中心 |  轻量级、可追溯、分级&延时、实时(长轮训)、重试、多模式（GLUE）
3   | xxl-conf       | GPL【TODO】(框架)         | 分布式配置中心 |  妙级推送、一致性、权限（appkey维度）、安全(灰度、可观测)     
4   | xxl-rpc        | Apache (框架)           | RPC服务中心 |  轻量级、高性能、全注解、运营（手动/自动、服务/节点禁用）、重构（appkey维度注册） 
5   | xxl-cache      | GPL【TODO】(框架)         | 二级缓存    |  两级缓存（redis+ caffeine）、全局热点(自定义规则)
6   | xxl-sso        | GPL                   | 单点登录中心  |  基础登录、鉴权(sdk化)、单点登录
7   | xxl-api        | GPL                   | API管理平台 |   API管理、权限隔离、Mock Server、自动化测试、swagger导入导出
8   | xxl-crawler    | Apache (框架)           | 爬虫框架    |  爬虫                 
9   | xxl-tool       | Apache (框架)           | 工具包     |  工具包，零依赖                
10  | xxl-boot       | GPL                   | 脚手架     |  快速开发框架、单体式、前后端分离             
11  | pages          | GPL                   | 官网      |  官网、博客 blog/notebook、文档 doc；
12  | xxl-incubator  | GPL                   | 孵化器     |   孵化器，脑暴             

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
