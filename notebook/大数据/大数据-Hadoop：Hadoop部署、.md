## 介绍

- 官网：http://hadoop.apache.org/ 
- 文档：http://hadoop.apache.org/docs/r1.0.4/cn/quickstart.html

Hadoop是一个由Apache基金会所开发的分布式系统基础架构。

用户可以在不了解分布式底层细节的情况下，开发分布式程序。充分利用集群的威力进行高速运算和存储。
[1] Hadoop实现了一个分布式文件系统（Hadoop Distributed File System），简称HDFS。HDFS有高容错性的特点，并且设计用来部署在低廉的（low-cost）硬件上；而且它提供高吞吐量（high throughput）来访问应用程序的数据，适合那些有着超大数据集（large data set）的应用程序。HDFS放宽了（relax）POSIX的要求，可以以流的形式访问（streaming access）文件系统中的数据。
Hadoop的框架最核心的设计就是：HDFS和MapReduce。HDFS为海量的数据提供了存储，则MapReduce为海量的数据提供了计算。

0.20.X系列： 0.20.2版本发布后，几个重要的特性没有基于trunk而是在0.20.2基础上继续研发。值得一提的主要有两个特性：Append与Security。其中，含Security特性的分支以0.20.203版本发布，而后续的0.20.205版本综合了这两个特性。需要注意的是，之后的1.0.0版本仅是0.20.205版本的重命名。0.20.X系列版本是最令用户感到疑惑的，因为它们具有的一些特性，trunk上没有；反之，trunk上有的一些特性，0.20.X系列版本却没有。

0.21.0/0.22.X系列：这一系列版本将整个Hadoop项目分割成三个独立的模块，分别是 Common、HDFS和MapReduce。HDFS和MapReduce都对Common模块有依赖性，但是MapReduce对HDFS并没有依赖性。这样，MapReduce可以更容易地运行其他分布式文件系统，同时，模块间可以独立开发。具体各个模块的改进如下。

    Common模块：最大的新特性是在测试方面添加了Large-Scale Automated Test Framework和Fault Injection Framework。 
    HDFS模块：主要增加的新特性包括支持追加操作与建立符号连接、Secondary NameNode改进（Secondary NameNode被剔除，取而代之的是Checkpoint Node，同时添加一个Backup Node的角色，作为            NameNode的冷备）、允许用户自定义block放置算法等。 
    MapReduce模块：在作业API方面，开始启动新MapReduce API，但老的API仍然兼容。 

0.22.0在0.21.0的基础上修复了一些bug并进行了部分优化。
    
0.23.X系列：0.23.X是为了克服Hadoop在扩展性和框架通用性方面的不足而提出来的。它实际上是一个全新的平台，包括分布式文件系统HDFS Federation和资源管理框架YARN两部分，可对接入的各种计算框架（如MapReduce、Spark等）进行统一管理。它的发行版自带MapReduce库，而该库集成了迄今为止所有的MapReduce新特性。

2.X系列：同0.23.X系列一样，2.X系列也属于下一代Hadoop。与0.23.X系列相比，2.X系列增加了NameNode HA和Wire-compatibility等新特性。


## 开发环境

环境部署：http://www.cnblogs.com/xia520pi/archive/2012/05/20/2510723.html

Eclipse安装hadoop插件：hadoop-eclipse-plugin-1.2.1.jar
(放到eclipse的plugins 目录下，重启 eclipse)

启动Hadoop，Eclipse配置Hadoop集群连接

## hadoop伪分布：CentOS + Hadoop

- 参考文档：http://blog.csdn.net/yinan9/article/details/16805275
- 官方文档：文档：http://hadoop.apache.org/docs/r1.0.4/cn/quickstart.html


    jdk：1.6.45（1.6系列最新稳定版）
    hadoop：hadoop-1.2.1（1.x系列最新稳定版）
    HBase：hbase-0.98.9
    Zookeeper：zookeeper-3.4.6（最新稳定版）
  
##### 前提、jdk安装，配置环境变量（忽略）；
      
```
# export JAVA_HOME=/usr/java/jdk1.6.0_45
# export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
# export PATH=$PATH:$JAVA_HOME/bin
```

##### 1、创建Hadoop用户及相关应用文件夹

- 1.1、同样使用root用户创建一个名为hadoop（hadoop_pwd）的新用户
```
# useradd hadoop
# passwd hadoop
```

- 1.2、创建应用文件夹，以便进行之后的hadoop配置
```
# mkdir /hadoop
# mkdir /hadoop/hdfs
# mkdir /hadoop/hdfs/data
# mkdir /hadoop/hdfs/name
# mkdir /hadoop/mapred
# mkdir /hadoop/mapred/local
# mkdir /hadoop/mapred/system
# mkdir /hadoop/tmp
```

- 1.3、将文件夹属主更改为hadoop用户
```
# chown -R hadoop /hadoop
```

##### 2、设置Hadoop用户使之可以免密码ssh到localhost：
```
# su - hadoop
# ssh-keygen -t dsa -P '' -f ~/.ssh/id_dsa  
# cat ~/.ssh/id_dsa.pub>> ~/.ssh/authorized_keys

# cd /home/hadoop/.ssh 
# chmod 600 authorized_keys
```

注意这里的权限问题，保证.ssh目录权限为700，authorized_keys为600

验证：
```    
# [hadoop@localhost .ssh]$ ssh localhost
Last login: Sun Nov 17 22:11:55 2013
```
命令ssh localhost之后无需输入密码就可以连接，配置OK

##### 3、安装配置hadoop：
```
重新切回root用户，创建目录并安装；
    # su
    # mkdir /opt/hadoop
下载，将安装文件移动到以上新建目录，确保其执行权限，然后执行
    # cd /home/hadoop
    # wget http://mirrors.hust.edu.cn/apache/hadoop/common/hadoop-1.2.1/hadoop-1.2.1.tar.gz       
    # tar -xzvf hadoop-1.2.1.tar.gz
    # mv /home/hadoop/hadoop-1.2.1  /opt/hadoop 
    # cd /opt/hadoop    
将hadoop安装目录的属主更改为hadoop用户
    # chown -R hadoop /opt/hadoop
```

##### 4、切换到hadoop用户，修改配置文件，这里根据前面创建的应用文件进行相关配置，依照各自情况而定

```
# su - hadoop
# cd /opt/hadoop/hadoop-1.2.1/conf

a、core-site.xml

<configuration>
    <property>
        <name>fs.default.name</name>
        <value>hdfs://localhost:9000</value>
    </property>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>/hadoop/tmp</value>
    </property>
</configuration>

b、hdfs-site.xml

<configuration> 
  <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <property>
        <name>dfs.name.dir</name>
        <value>/hadoop/hdfs/name</value>
    </property>
    <property>
        <name>dfs.data.dir</name>
        <value>/hadoop/hdfs/data</value>
    </property>
</configuration>

c、mapred-site.xml

<configuration>
  <property>
        <name>mapred.job.tracker</name>
        <value>localhost:9001</value>
    </property>
</configuration>

d、hadoop-env.sh

配置JAVA_HOME 与 HADOOP_HOME_WARN_SUPPRESS。
    PS：HADOOP_HOME_WARN_SUPPRESS这个变量可以避免某些情况下出现这样的提醒 "WARM: HADOOP_HOME is deprecated”
    # export JAVA_HOME = /usr/local/java/jdk1.6.0_45
    # export HADOOP_HOME_WARN_SUPPRESS="TRUE"    
使更新后的配置文件生效
    # source hadoop-env.sh
```

##### 5、重新配置 /etc/profile 文件，最终如：
```        
# su
# vi /etc/profile
//# export JAVA_HOME=/usr/java/jdk1.6.0_45
//# export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
# export HADOOP_HOME=/opt/hadoop/hadoop-1.2.1  
# export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin

使更新后的配置文件生效

# source /etc/profile
   
测试hadoop安装：

# hadoop version  

显示 Hadoop 1.2.1 表示安装成功。
```

##### 6、启动HADOOP
```
    需要先格式化namenode，再启动所有服务
        # su hadoop  (面ssh，不需要重复输入账号密码)
        # cd /opt/hadoop/hadoop-1.2.1/bin
        # hadoop namenode -format
        # start-all.sh 
```

##### 查看进程
```
        # jps   
    显示如下
        6360 NameNode  
        6481 DataNode  
        6956 Jps  
        6818 TaskTracker  
        6610 SecondaryNameNode  
        6698 JobTracker 
    如果能找到这些服务，说明Hadoop已经成功启动了。
    如果有什么问题，可以去/opt/hadoop/hadoop-1.2.1/logs查看相应的日志
    
    问题01：namenode启动不了：
    解决方式1：重启
        # cd /opt/hadoop/hadoop-1.2.1/bin
        # stop-all.sh
        # rm  -rf /hadoop/tmp/*
        # hadoop namenode -format
        # start-all.sh 
    解决方式2：core-site.xml更该hadoop.tmp.dir的目录
        <value>/tmp/hadoop/hadoop-${user.name}</value>
    问题02：localhost:9000 not available yet, Zzzzz... 
    解决方式：
        # rm  -rf /hadoop/tmp/*
        修改/etc/hosts文件，新增映射：192.168.40.128 master    
        修改文件：core-site.xml 和 mapred-site.xml中的localhost为master
```

##### 7、最后就可以通过以下链接访问haddop服务了
```
-------开启端口：保存修改（save后永久）--
# /sbin/iptables -I INPUT -p tcp --dport 50030 -j ACCEPT
# /sbin/iptables -I INPUT -p tcp --dport 50060 -j ACCEPT
# /sbin/iptables -I INPUT -p tcp --dport 50070 -j ACCEPT
# /etc/rc.d/init.d/iptables save

http://localhost:50030/       for the Jobtracker
http://localhost:50060/       for the Tasktracker
http://localhost:50070/       for the Namenode
```


##### PS：完全分布式的安装与伪分布式安装大同小异，注意如下几点即可

    1.集群内ssh免用户登录
    2.配置文件中指定具体的ip地址(或机器名)，而不是localhost
    3.配置masters和slaves文件，加入相关ip地址(或机器名)即可
    （以上配置需要在各个节点上保持一致。）


## Hadoop分布式：CentOS + Hadoop完全分布式

官方文档：文档：http://hadoop.apache.org/docs/r1.0.4/cn/quickstart.html

