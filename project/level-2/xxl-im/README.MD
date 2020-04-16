《分布式消息推送平台 XXL-PUSH》

项目：
---
- xxl-push-broker   : 推送代理中心，挂载"xxl-push-core"（ws + redis集群/beat刷新、连接监控、消息监控）
- xxl-push-core     : 推送核心逻辑
- xxl-push-im       : 基于xxl-push实现的IM系统, 拥有push、IM特性

redis key：
---
xxl-push.broker.registry=Set(IP:PORT, freshTime)        ： Broker注册，可感知所有在线的Broker集群；
xxl-push.broker.msg.BrokerXAddress=Queue(推送的消息)     ：Broker对应的消息Queue
xxl-push.biz.conns.BizX=Set(ConnID, BrokerXAddress)     : 每条业务线，在线连接数

channel管理：可监控所有在线的WS连接信息
- BIZ-ID：每个push业务，对应一个唯一的BizID；
- CONN-ID：每个push业务下可推送消息的client对应一个唯一的CONN-ID，推荐该ID为有意义的ID，例如UserID；

PUSH：推送消息给channel的client端
- 全量推送：推送给指定BizID下，所有的在线的CONN；
- 批量推送：推送给指定ConnID列表对应的CONN；
- 单点推送：推送给指定一个ConnID对应的CONN；

消息PUSH流程：
1、消息，指定Biz 和 ConnX；
2、从 "xxl-push.biz.conns.BizX" 定位到对应 "BrokerX"，然后向 "xxl-push.broker.msg.BrokerXAddress" 队列push消息；
3、"BrokerX" 从队列pull消息，匹配对应ConnX的channel，推送消息；
4、ConnX接受到推送的消息；