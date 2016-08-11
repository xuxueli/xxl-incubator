package com.xxl.util.core.util;

import net.spy.memcached.compat.log.Logger;
import net.spy.memcached.compat.log.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * Redis client base on jedis 根据继承类的不同,
 * jedis实例方式不用:JedisSimpleFactry/JedisPoolFactry/ShardedJedisPoolFactry
 * 
 * @author xuxueli 2015-7-10 18:34:07
 *
 *	<!-- jedis:redis clients -->
 *	<dependency>
 *		<groupId>redis.clients</groupId>
 *		<artifactId>jedis</artifactId>
 *		<version>2.9.0</version>
 *	</dependency>
 *
 * 	# for redis (sharded.jedis.address=host01:port,host02:port)
 *	sharded.jedis.address=127.0.0.1:6379,127.0.0.1:6379,127.0.0.1:6379
 *
 */
public class JedisUtil {
	private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
	private static final int DEFAULT_EXPIRE_TIME = 7200; // 默认过期时间,单位/秒, 60*60*2=2H, 两小时

	// ------------------------ ShardedJedisPool ------------------------
	/**
	 *	方式01: Redis单节点 + Jedis单例 : Redis单节点压力过重, Jedis单例存在并发瓶颈 》》不可用于线上
	 * 		new Jedis("127.0.0.1", 6379).get("cache_key");
	 *	方式02: Redis单节点 + JedisPool单节点连接池 》》 Redis单节点压力过重，负载和容灾比较差
	 * 		new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 10000).getResource().get("cache_key");
	 *  方式03: Redis集群(通过client端集群,一致性哈希方式实现) + Jedis多节点连接池 》》Redis集群,负载和容灾较好, ShardedJedisPool一致性哈希分片,读写均匀，动态扩充
	 *  	new ShardedJedisPool(new JedisPoolConfig(), new LinkedList<JedisShardInfo>());
     */

	private static ShardedJedisPool shardedJedisPool;
	// static {getInstance();}

	/**
	 * 获取ShardedJedis实例
	 * @return
	 */
	private static ShardedJedis getInstance() {
		if (shardedJedisPool == null) {
			synchronized (JedisUtil.class) {
				// JedisPoolConfig
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(1024);            // # jedis的最大分配对象
				config.setMaxIdle(200);            // # jedis最大保存idel状态对象数
				config.setMaxWaitMillis(10000);        // # jedis池没有对象返回时，最大等待时间
				config.setTestOnBorrow(true);        // # jedis调用borrowObject方法时，是否进行有效检查
				config.setTestOnReturn(true);        // # jedis调用returnObject方法时，是否进行有效检查

				// JedisShardInfo List
				List<JedisShardInfo> jedisShardInfos = new LinkedList<JedisShardInfo>();

				Properties prop = PropertiesUtil.loadProperties("cache.properties");
				String address = PropertiesUtil.getString(prop, "sharded.jedis.address");

				String[] addressArr = address.split(",");
				for (int i = 0; i < addressArr.length; i++) {
					String[] addressInfo = addressArr[i].split(":");
					String host = addressInfo[0];
					int port = Integer.valueOf(addressInfo[1]);
					JedisShardInfo jedisShardInfo = new JedisShardInfo(host, port, 10000);
					jedisShardInfos.add(jedisShardInfo);
				}
				shardedJedisPool = new ShardedJedisPool(config, jedisShardInfos);
				logger.info(">>>>>>>>>>> xxl-cache, JedisUtil.ShardedJedisPool init success.");
			}
		}

		if (shardedJedisPool == null) {
			throw new NullPointerException(">>>>>>>>>>> xxl-cache, JedisUtil.ShardedJedisPool is null.");
		}

		ShardedJedis shardedJedis = shardedJedisPool.getResource();
		return shardedJedis;
	}

	/**
	 * 释放ShardedJedis资源
	 * @param shardJedis	采用redis池方式调用,必须返还实例，否则链接占用过多，会导致异常：JedisConnectionException: Could not get a resource from the pool
	 */
	public static void returnInstance(final ShardedJedis  shardJedis) {
		if (shardJedis != null) {
			shardedJedisPool.returnResourceObject(shardJedis);
		}
	}

	// ------------------------ serialize and unserialize ------------------------
	/**
	 * 将对象-->byte[] (由于jedis中不支持直接存储object所以转换成byte[]存入)
	 * 
	 * @param object
	 * @return
	 */
	private static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes = baos.toByteArray();
			return bytes;
		} catch (Exception e) {
			logger.error("{}", e);
		} finally {
			try {
				oos.close();
				baos.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}
		return null;
	}

	/**
	 * 将byte[] -->Object
	 * 
	 * @param bytes
	 * @return
	 */
	private static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			logger.error("{}", e);
		} finally {
			try {
				bais.close();
			} catch (IOException e) {
				logger.error("{}", e);
			}
		}
		return null;
	}

	// ------------------------ jedis util ------------------------
	/**
	 * 存储简单的字符串或者是Object 因为jedis没有分装直接存储Object的方法，所以在存储对象需斟酌下
	 * 存储对象的字段是不是非常多而且是不是每个字段都用到，如果是的话那建议直接存储对象，
	 * 否则建议用集合的方式存储，因为redis可以针对集合进行日常的操作很方便而且还可以节省空间
	 */

	/**
	 * Set String
	 * @param key
	 * @param value
	 * @param seconds	存活时间,单位/秒
     * @return
     */
	public static String setStringValue(String key, String value, int seconds) {
		String result = null;
		ShardedJedis client = getInstance();
		try {
			result = client.setex(key, seconds, value);
		} catch (Exception e) {
			logger.info("{}", e);
		} finally {
			returnInstance(client);
		}
		return result;
	}

	/**
	 * Set String (默认存活时间, 2H)
	 * @param key
	 * @param value
     * @return
     */
	public static String setStringValue(String key, String value) {
		return setStringValue(key, value, DEFAULT_EXPIRE_TIME);
	}

	/**
	 * Set Object
	 * 
	 * @param key
	 * @param obj
	 * @param seconds	存活时间,单位/秒
	 */
	public static String setObjectValue(String key, Object obj, int seconds) {
		String result = null;
		ShardedJedis client = getInstance();
		try {
			result = client.setex(key.getBytes(), seconds, serialize(obj));
		} catch (Exception e) {
			logger.info("{}", e);
		} finally {
			returnInstance(client);
		}
		return result;
	}

	/**
	 * Set Object (默认存活时间, 2H)
	 * @param key
	 * @param obj
     * @return
     */
	public static String setObjectValue(String key, Object obj) {
		return setObjectValue(key, obj, DEFAULT_EXPIRE_TIME);
	}

	/**
	 * Get String
	 * @param key
	 * @return
	 */
	public static String getStringValue(String key) {
		String value = null;
		ShardedJedis client = getInstance();
		try {
			value = client.get(key);
		} catch (Exception e) {
			logger.info("", e);
		} finally {
			returnInstance(client);
		}
		return value;
	}

	/**
	 * Get Object
	 * @param key
	 * @return
     */
	public static Object getObjectValue(String key) {
		Object obj = null;
		ShardedJedis client = getInstance();
		try {
			byte[] bytes = client.get(key.getBytes());
			if (bytes != null && bytes.length > 0) {
				obj = unserialize(bytes);
			}
		} catch (Exception e) {
			logger.info("", e);
		} finally {
			returnInstance(client);
		}
		return obj;
	}

	/**
	 * Delete
	 * @param key
	 * @return Integer reply, specifically:
	 * 		an integer greater than 0 if one or more keys were removed
	 *      0 if none of the specified key existed
	 */
	public static Long del(String key) {
		Long result = null;
		ShardedJedis client = getInstance();
		try {
			result = client.del(key);
		} catch (Exception e) {
			logger.info("{}", e);
		} finally {
			returnInstance(client);
		}
		return result;
	}

	/**
	 * incrBy	value值加i
	 * @param key
	 * @param i
	 * @return new value after incr
	 */
	public static Long incrBy(String key, int i) {
		Long result = null;
		ShardedJedis client = getInstance();
		try {
			result = client.incrBy(key, i);
		} catch (Exception e) {
			logger.info("{}", e);
		} finally {
			returnInstance(client);
		}
		return result;
	}

	/**
	 * exists
	 * @param key
	 * @return Boolean reply, true if the key exists, otherwise false
	 */
	public static boolean exists(String key) {
		Boolean result = null;
		ShardedJedis client = getInstance();
		try {
			result = client.exists(key);
		} catch (Exception e) {
			logger.info("{}", e);
		} finally {
			returnInstance(client);
		}
		return result;
	}

	/**
	 * expire	重置存活时间
	 * @param key
	 * @param seconds	存活时间,单位/秒
	 * @return Integer reply, specifically:
	 * 		1: the timeout was set.
	 * 		0: the timeout was not set since the key already has an associated timeout (versions lt 2.1.3), or the key does not exist.
	 */
	public static long expire(String key, int seconds) {
		Long result = null;
		ShardedJedis client = getInstance();
		try {
			result = client.expire(key, seconds);
		} catch (Exception e) {
			logger.info("{}", e);
		} finally {
			returnInstance(client);
		}
		return result;
	}

	/**
	 * expireAt		设置存活截止时间
	 * @param key
	 * @param unixTime		存活截止时间戳
	 * @return
	 */
	public static long expireAt(String key, long unixTime) {
		Long result = null;
		ShardedJedis client = getInstance();
		try {
			result = client.expireAt(key, unixTime);
		} catch (Exception e) {
			logger.info("{}", e);
		} finally {
			returnInstance(client);
		}
		return result;
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			setObjectValue("key" + i, "value" + i);
		}
		System.out.println(getObjectValue("key" + 5555));
		long end = System.currentTimeMillis();
		System.out.println("cost:" + (end - start));

		// JedisSimpleFactry-100000:11996/11938/11848
		// JedisSimpleFactry-100000:35061
		// ShardedJedisPoolFactry-100000:8G:84449		4G:235939

	}

}