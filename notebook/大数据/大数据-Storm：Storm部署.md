- [官方文档](http://storm.apache.org/releases/1.1.1/index.html)
- [文章1](http://www.cnblogs.com/quchunhui/p/5370191.html)

#### Storm简介
- Storm是开源的、分布式、流式计算系统。
- 分布式：将一个任务拆解给多个计算机去执行，让多机器共同完成同一个任务。
- Storm采用的是主从结构
    - Nimbus/主节点/老板：
        - 只负责整体分配工作
        - 不具体干活
    - Supervisor/从节点/小组经理
        - 直接管理干活的Worker
    - Worker：
        - 工作（执行TASK）进程
- 主从结构优点
    - 主从结构：简单、高效，主节点存在单点问题
    - 对称结构：复杂、低效，但无单点问题，更可靠
- Storm作业提交运行流程
    - 1、用户使用Storm的API来编写Storm Topology
    - 2、使用Storm的Client将Topology提交给Nimbus
    - 3、Nimbus收到之后，会将把这些Topology分配给足够的Supervisor
    - 4、Supervisor收到这些Topoligy之后，Nimbus会指派一些Task给这些Supervisor。
    - 5、Nimvus会指示Supervisor为这些Task生成一些Worker
    - 6、Worker来执行这些Task来完成计算任务
- Topology = 拓扑 = 作业：
    - 点和边组成的一个有向无环图
    - 结构
        - Spout：数据源节点（水龙头，发送Tuple给下游的Bolt）
        - Bolt：普通的计算节点（发送一个Tuple给下一个Bolt；可以执行一些写数据到外部存储；）
        - Stream：数据流，点之间的边
        - Tuple：数据流中的每一条记录
    
<img src="http://images2015.cnblogs.com/blog/915691/201604/915691-20160409202101812-875140827.png" width="300" >
<img src="http://images2015.cnblogs.com/blog/915691/201604/915691-20160409202522187-1038237110.png" width="300" >

#### 组件说明
Topologies：一个topology就是一个计算节点所组成的图


#### 流式计算和批量计算区别
-- | 批量计算(Hadoop为代表) | 流式计算(Storm为代表)
--- | --- | ---
数据到达 | 计算开始前数据已准备好 | 计算进行中数据持续到来
计算周期 | 计算完成后会结束计算 | 时效性要求低的场景
使用场景 | 一般会作为服务持续运行 | 时效性要求高的场景

#### MapReduce和Storm的工作流程对比
数据特点 | 实例特点 | 实现思想 | 技术选型 | 编程模型
--- | --- | --- | --- | ---
海量、固定规模 | 批量处理 | 分而治之 | Hadoop | Map + Reduce
海量、持续增加 | 流式实时处理 | 分而治之 | Storm | Spout + Bolt

- MapReduce：Hdfs》Mapper》Reducer》Hdfs
- Storm：DataSource》Spout》SplitBolt》CountBolt》DataSink






