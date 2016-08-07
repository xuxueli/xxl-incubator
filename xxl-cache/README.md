
##### 项目拆分
- xxl-cache-admin   : 缓存管理中心,维护缓存Key(新增、删除),维护缓存内容(清除)
- xxl-cache-service : 缓存的RPC服务,统一线上缓存API操作流程,如:Set、Get、Remove等操作;
- xxl-cache-core    : 公共依赖,接入xxl-cache的项目依赖该JAR即可使用xxl-cache;
- xxl-cache-examples: 接入xxl-cache的Demo项目,可以参考该项目接入xxl-cache

概念:
- 缓存Key: xxl-cache中每个缓存的唯一标示,必须通过该Key才允许操作缓存,负责服务端拒绝服务;

##### 汇总
 * ehcache 进程缓存, 虽然目前ehcache提供集群方案，但是分布式缓存还是使用memcached/redis比较好;
 * memcached 分布式缓存, 分布是通常通过分片方式实现, 多核, 数据结构单一, key250k和value1M容量首限;
 * redis 分布式缓存, 单核, 支持数据结构丰富, key和value512M容量巨大;
 * mongodb Nosql, 非关系型数据库(平级于mysql)，存储海量数据;
 * <p/>
 * ehcache本地对象缓存：EhcacheUtil (ehcacheObj)
 * ehcache页面缓存：SimplePageCachingFilter (ehcacheUrl)