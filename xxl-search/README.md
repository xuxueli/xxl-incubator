# xxl-search

搜索小程序, 移植点评

mq移植dianping

search增强es实现



----------------

### 简介
apache lucene是apache下一个著名的开源搜索引擎内核，基于Java技术，处理索引，拼写检查，点击高亮和其他分析，分词等技术。

### 作用
- 1、性能上：大数据量的情况下，用数据库来做文本的搜索是很可怕的事情，但是Lucene轻松毫秒杀
- 2、功能上：Lucene 可以做全文匹配搜索
    - 假设："开源中国"是一个以开源软件为核心的技术社区网站……，
    - 搜索关键字："开源社区"
    - 如果这个内容是存在数据库的，就搜不到，如果使用Lucene就可以搜到

### 文档
[官网](http://lucene.apache.org/core/)
[文档5.5.2](http://lucene.apache.org/core/5_5_2/index.html)
[客户端--索引文件查询工具luck](https://github.com/DmitryKey/luke/releases)

[文档--lucene使用与优化](http://my.oschina.net/lushuifa/blog/198690)
[文档--lucene学习笔记](http://my.oschina.net/kkrgwbj/blog/513362)


# Elasticsearch 应用
---
[官网下载发行版](https://www.elastic.co/downloads/elasticsearch)
[github源码](https://github.com/elastic/elasticsearch/releases)
[文档](https://github.com/elastic/elasticsearch/blob/master/docs/java-api/client.asciidoc)


### 简介 : A Distributed RESTful Search Engine
Lucene可以说是当今最先进，最高效的全功能开源搜索引擎框架。但是**Lucene只是一个框架，要充分利用它的功能，需要使用JAVA，并且在程序中集成Lucene**。需要很多的学习了解，才能明白它是如何运行的，Lucene确实非常复杂。

**Elasticsearch是一个建立在全文搜索引擎 Apache Lucene™ 基础上的搜索引擎**。

Elasticsearch使用Lucene作为内部引擎，但是在使用它做全文搜索时，只需要使用统一开发好的API即可，而不需要了解其背后复杂的Lucene的运行原理。

当然Elasticsearch并不仅仅是Lucene这么简单，它不但包括了全文搜索功能，还可以进行以下工作:
    - 分布式实时文件存储，并将每一个字段都编入索引，使其可以被搜索。
    - 实时分析的分布式搜索引擎。(lucene必须事先跑完索引,才可以提供完整的搜索服务,有延迟)
    - 可以扩展到上百台服务器，处理PB级别的结构化或非结构化数据。
    - 这么多的功能被集成到一台服务器上，你可以轻松地通过客户端或者任何你喜欢的程序语言与ES的RESTful API进行交流。

Elasticsearch的上手是非常简单的。它附带了很多非常合理的默认值，这让初学者很好地避免一上手就要面对复杂的理论，它安装好了就可以使用了，用很小的学习成本就可以变得很有生产力。

#### 索引文件结构
- Index：这是ES存储数据的地方，类似于关系数据库的database。
- Document type：嗯，类似关系数据库的表，主要功能是将完全不同schema（这个概念以后会讲到，不急）的数据分开，一个index里面可以有若干个Document type。
- Document：好吧，这个类似关系数据库的一行，在同一个Document type下面，每一Document都有一个唯一的ID作为区分；
- Filed：类似关系数据库的某一列，这是ES数据存储的最小单位。
- Cluster和Node：ES可以以单点或者集群方式运行，以一个整体对外提供search服务的所有节点组成cluster，组成这个cluster的各个节点叫做node。
- shard：通常叫分片，这是ES提供分布式搜索的基础，其含义为将一个完整的index分成若干部分存储在相同或不同的节点上，这些组成index的部分就叫做shard。
- Replica：和replication通常指的都是一回事，即index的冗余备份，可以用于防止数据丢失，或者用来做负载分担。


### 安装部署 Elasticsearch 
- 1、Elasticsearch安装：官网下载Zip安装包解压目录。执行 "/bin/elasticsearch.bat" 启动脚本, 然后访问：http://localhost:9200
- 2、ES集群管理工具安装, H5编写: 切换bin目录下，cmd执行："plugin install mobz/elasticsearch-head"  , 然后访问：http://localhost:9200/_plugin/head/ 
- 3、基本配置：修改 "/config/elasticsearch.yml" 文件如下：
```
# 配置集群的名字，为了能进行自动查找
cluster.name: elasticsearch-cluster-centos
# 配置当前节点的名字，当然每个节点的名字都应该是唯一的
node.name: "es-node2"
# 为节点之间的通信设置一个自定义端口(默认为9300)  
transport.tcp.port: 9301 
# 绑定host，0.0.0.0代表所有IP，为了安全考虑，建议设置为内网IP
network.host: "127.0.0.1"
# 对外提供http服务的端口，安全考虑，建议修改，不用默认的9200
http.port: 9201
# 表示这个节点是否可以充当主节点，这个节点是否充当数据节点,如果你的节点数目只有两个的话，为了防止脑裂的情况，需要手动设置主节点和数据节点。其他情况建议直接不设置，默认两个都为true.
node.master: false
node.data: true
# 集群自动发现机制 (单机时,忽略)
discovery.zen.ping.multicast.enabled: false        // 把组播的自动发现给关闭了，为了防止其他机器上的节点自动连入。
discovery.zen.fd.ping_timeout: 100s                // 节点与节点之间的连接ping时长
discovery.zen.ping.timeout: 100s
discovery.zen.minimum_master_nodes: 2              // 设置为了避免脑裂。比如3个节点的集群，如果设置为2，那么当一台节点脱离后，不会自动成为master。
discovery.zen.ping.unicast.hosts: ["127.0.0.1:9301"]     // 关闭了自动创建索引。为的也是安全考虑，否则即使是内网，也有很多扫描程序，一旦开启，扫描程序会自动给你创建很多索引

另外：在bin/elasticsearch里面增加两行：
ES_HEAP_SIZE=4g
MAX_OPEN_FILES=65535
这两行设置了节点可以使用的内存数和最大打开的文件描述符数。
```
- 4、自动选举：
    
    - **master-slave**: elasticsearch集群一旦建立起来以后，会选举出一个master，其他都为slave节点。 但是具体操作的时候，每个节点都提供写和读的操作。就是说，你不论往哪个节点中做写操作，这个数据也会分配到集群上的所有节点中。
    
    - **replicate**: 这里有某个节点挂掉的情况，如果是slave节点挂掉了，那么首先关心，数据会不会丢呢？不会。如果你开启了replicate，那么这个数据一定在别的机器上是有备份的。别的节点上的备份分片会自动升格为这份分片数据的主分片。这里要注意的是这里会有一小段时间的yellow状态时间。

    - **防止脑裂(discovery.zen.minimum_master_nodes)**: 
 
    如果是主节点挂掉怎么办呢？当从节点们发现和主节点连接不上了，那么他们会自己决定再选举出一个节点为主节点。
    
    但是这里有个脑裂的问题，假设有5台机器，3台在一个机房，2台在另一个机房，当两个机房之间的联系断了之后，每个机房的节点会自己聚会，推举出一个主节点。这个时候就有两个主节点存在了，当机房之间的联系恢复了之后，这个时候就会出现数据冲突了。
    
    解决的办法就是设置参数： discovery.zen.minimum_master_nodes 为3(超过一半的节点数)，那么当两个机房的连接断了之后，就会以大于等于3的机房的master为主，另外一个机房的节点就停止服务了。
    
    - **负载均航(RESTful API)** :对于自动服务这里不难看出，如果把节点直接暴露在外面，不管怎么切换master，必然会有单节点问题。所以一般我们会在可提供服务的节点前面加一个负载均衡。

- 5、自动发现：
    elasticsearch的集群是内嵌自动发现功能的。
    
    意思就是说，你只需要在每个节点配置好了集群名称，节点名称，互相通信的节点会根据es自定义的服务发现协议去按照多播的方式来寻找网络上配置在同样集群内的节点。
    
    和其他的服务发现功能一样，es是支持多播（本机）和单播的。多播和单播的配置分别根据这几个参数（上吻参数“# 集群自动发现机制”）

    多播是需要看服务器是否支持的，由于其安全性，其实现在基本的云服务（比如阿里云）是不支持多播的，所以即使你开启了多播模式，你也仅仅只能找到本机上的节点。

    单播模式安全，也高效，但是缺点就是如果增加了一个新的机器的话，就需要每个节点上进行配置才生效了。

