package com.xxl.util.core.util;

import com.schooner.MemCached.MemcachedItem;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

import java.util.Properties;

/**
 * Memcached客户端工具类(Base on Memcached-Java-Client)
 * Created by xuxueli on 16/8/11.
 *
 *  <!-- Memcached-Java-Client -->
 *  <dependency>
 *      <groupId>com.whalin</groupId>
 *      <artifactId>Memcached-Java-Client</artifactId>
 *      <version>3.0.2</version>
 *  </dependency>
 *
 *  # for memcached.java.client (memcached.java.client.address="host01:port,host02:port", memcached.java.client.weights="5,5")
 *  memcached.java.client.address=192.168.56.101:11211
 *  memcached.java.client.weights=100
 *
 *  新版本2.6.1版本以后,支持apache-commoms-pool作为连接池。 支持权重配置。
 */
public class MemcachedJavaClientUtil {

    protected static MemCachedClient mcc = new MemCachedClient();

    // set up connection pool once at class load
    static {
        // server list and weights

        Properties prop = PropertiesUtil.loadProperties("cache.properties");
        // client地址
        String[] servers = PropertiesUtil.getString(prop, "memcached.java.client.address").split(",");

        // client权重
        String[] weightsArr = PropertiesUtil.getString(prop, "memcached.java.client.weights").split(",");
        Integer[] weights = new Integer[weightsArr.length];
        for (int i = 0; i < weightsArr.length; i++) {
            weights[i] = Integer.parseInt(weightsArr[i]);
        }

        // grab an instance of our connection pool
        SockIOPool pool = SockIOPool.getInstance();

        // set the servers and the weights
        pool.setServers(servers);
        pool.setWeights(weights);
        pool.setHashingAlg(SockIOPool.CONSISTENT_HASH);

        // set some basic pool settings
        // 5 initial, 5 min, and 250 max conns
        // and set the max idle time for a conn
        // to 6 hours
        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        pool.setMaxIdle(1000 * 60 * 60 * 6);

        // set the sleep for the maint thread
        // it will wake up every x seconds and
        // maintain the pool size
        pool.setMaintSleep(30);

        // set some TCP settings
        // disable nagle
        // set the read timeout to 3 secs
        // and don't set a connect timeout
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);

        // initialize the connection pool
        pool.initialize();
    }

    /**
     * Get方法
     * @param key
     * @return
     */
    public static Object get(String key){
        return mcc.get(key);
    }

    public static void main(String[] args) {
        System.out.println("SET: " + mcc.set("key1", "value1"));
        System.out.println("SET: " + mcc.set("key2", "value2"));
        System.out.println("SET: " + mcc.set("key3", "value3"));
        System.out.println("GET: " + mcc.get("key1"));

        MemcachedItem item = mcc.gets("key1");
        System.out.println("GETS: value=" + item.getValue() + ",CasUnique:"+item.getCasUnique());
        System.out.println("SET: " + mcc.set("key1", "value1_1"));
        System.out.println("CAS: " + mcc.cas("key1", "value1_2", item.getCasUnique())); // 必须FALSE (cas:原子写操作,必须版本号一致才允许写入)
        System.out.println("getMulti:" + mcc.getMulti(new String[]{"key1","key2","key3"}));
    }

}
