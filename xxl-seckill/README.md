# xxl-seckill

秒杀系统，先到先得-队列 、VIP优先、黑白名单、随机抽取、私人定制



例如：5000人抢购procuct_id=9999的产品，产品库存procuct_seckill_count=10个；
订单状态：未开始》秒杀中》秒杀成功/失败；

##### 原理：
- 1、秒杀页面静态化：秒杀页面html静态化，同步CDN，；
- 2、防机器刷单：一个userId + 一个productId，5s内置允许请求一次【procuctid_userid_${procuct_id}_${userid}=5】；
- 3、缓存减压-：缓存key = seckill_procuct_cache_${procuctId} = ${procuct_seckill_count} * 1.5；
- 4、队列秒杀-先到先得- ：分布式队列接收秒杀请求，先到先得，异步更新秒杀结果；
- 5、黑白名单-分组；
