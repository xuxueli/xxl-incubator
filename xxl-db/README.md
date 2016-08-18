# xxl-db
分布式数据库中间件

目标：
1、读写分离：Abstract Router方式；
2、分库分表（分表）：指定字段按照指定规则-id/日期，拆分多张表，存放单库或多库；解决分表情况下CRUD；




《分布式数据库中间件：xx-db》

git地址：https://github.com/xuxueli/xxl-incubator


zebra : https://github.com/ainilife/zebra-dao


#### 1、问题：
- 1、单个表数据量太大
- 2、单个库数据量太大
- 3、单台数据量服务器压力很大
- 4、读写速度遇到瓶颈
 
#### 2、解决方案：
- **1、向上扩展(scale** up)：增加单台机器硬件性能。（只能暂时解决问题，当业务量不断增长时还是解决不了问题。特别是淘宝，facebook，youtube这种业务成线性，甚至指数级上升的情况）
- **2、水平扩展：** 直接增加机器数量，把数据库放到不同服务器上，在应用到数据库之间加一个proxy进行路由，这样就可以解决上面的问题了。
    - **2.1、master-slave读写分离**：针对整个库，每台机器上都有库中全部数据表，master-slave同步模式；（驴妈妈，三台oracle；点评，mysql）
    - **2.2、分库分表**：（分散单台机器压力）
        - 2.3.1、分库（数据的垂直切分）：（表Sharding）根据不同的表（Schema）来切分到不同的数据库机器上，简单清晰；（波克城市，8台oracle，各自为政）
        - 2.3.2、分表（数据的水平切分）：（数据Sharding）根据表中数据逻辑关系，将同一个张表中的数据按照某种条件拆分到多台数据库机器上去，拆分规则较复杂；
（tips：表垂直拆分属于业务扩展，不要混淆）

#### 3、水平扩展，需要解决的目标：
- **1、读写分离**：红线代表写请求，绿线代表读请求，这就是一个简单的读写分离

![image](http://static.open-open.com/news/uploadImg/20151119/20151119212723_279.jpg)

- **2、分库分表，详解** = 分库 + 分表；

![image](http://static.open-open.com/news/uploadImg/20151119/20151119212723_98.jpg)

上面这幅图就可以看出中间件作用，比如下面的这个SQL：
```select * from table_name where id = 1;```

按照中间件分库分表算法，此SQL将发送到DB1节点，由DB1这个MySQL负责解析和获取id=1的数据，并通过中间件返回给客户端。而在读写分离结构中并没有这些分库分表规则， 他只能在众多读节点中load balance随机进行分发，它要求各个节点都要存放一份完整的数据。

#### 流行的各类中间件比较

目前市面上中间件种类很多种 先看下各种中间件背景:

![image](http://static.open-open.com/news/uploadImg/20151119/20151119212724_752.jpg)

mycat官网：http://www.mycat.org.cn/

mycat的github：https://github.com/MyCATApache/MyCAT-Server/
