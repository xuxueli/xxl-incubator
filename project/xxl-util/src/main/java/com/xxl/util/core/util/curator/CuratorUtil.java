package com.xxl.util.core.util.curator;

import com.xxl.util.core.util.Environment;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.RetryNTimes;

/**
 * curator tool
 *
 * @author xuxueli 2018-05-09 20:45:51
 */
public class CuratorUtil {

    public static String zkAddress = Environment.ZK_ADDRESS;
    public static String zkPath = "/xxl-conf/default.key01";

    public static void main(String[] args) throws Exception {
        listenerTest();

        //optTest();
    }

    // 测试监听失败
    private static void listenerTest() throws InterruptedException {
        CuratorFramework zkClient = CuratorFrameworkFactory.newClient(zkAddress, new RetryNTimes(10, 5000));
        zkClient.start();
        System.out.println("start zkclient...");


        final NodeCache nodeCache = new NodeCache(zkClient, zkPath, false);
        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                System.out.println("当前节点：" + nodeCache.getCurrentData());
            }
        });

        System.out.println("register wathcer end...");
        Thread.sleep(Integer.MAX_VALUE);
        zkClient.close();
    }


    private static void optTest() throws Exception {

        // Connect
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddress, new RetryNTimes(10, 5000));
        client.start();

        // Create
        String data1 = "hello";
        client.create().creatingParentsIfNeeded().forPath(zkPath, data1.getBytes());

        // Get
        System.out.println("ls: " + client.getChildren().forPath("/") );
        System.out.println("get:" + zkPath + "=" + new String(client.getData().forPath(zkPath)));

        // Modify
        client.setData().forPath(zkPath, "world".getBytes());
        System.out.println("get:" + zkPath + "=" + new String(client.getData().forPath(zkPath)));

        // Remove
        client.delete().forPath(zkPath);
        System.out.println("ls: " + client.getChildren().forPath("/") );
    }

}
