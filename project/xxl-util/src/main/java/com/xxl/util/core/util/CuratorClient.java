package com.xxl.util.core.util;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * curator client
 *
 *      1、自动重连；
 *      2、节点创建，级联parent
 *      3、节点删除，级联child
 *
 * @author xuxueli 2017-08-23 20:23:17
 */
public class CuratorClient {

    // ---------------------- client ----------------------

    private CuratorFramework client;

    /**
     * @param connectString
     *      like "ip:2181" or "ip01:2181,ip02:2181,ip03:2181"
     * @param newNamespace
     *      use specified namespace "/xxl-conf" or null
     */
    public CuratorClient(String connectString, String newNamespace) {
        client = CuratorFrameworkFactory.builder().
                connectString(connectString).
                namespace(newNamespace).
                sessionTimeoutMs(60 * 1000).
                connectionTimeoutMs(15 * 1000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                build();

        client.start();
    }

    /**
     * destory
     */
    public void destory() {
        if (client != null) {
            client.close();
        }
    }


    /**
     * 获取 client
     *
     * @return
     */
    public CuratorFramework getClient() {
        return client;
    }


    // ---------------------- node ----------------------

    /**
     * 创建 node
     *
     * @param nodeName
     * @param value
     * @return
     */
    public boolean createNode(String nodeName, String value) {
        boolean suc = false;
        try {
            Stat stat = getClient().checkExists().forPath(nodeName);
            if (stat == null) {
                String opResult = null;
                if (Strings.isNullOrEmpty(value)) {
                    opResult = getClient().create().creatingParentsIfNeeded().forPath(nodeName);
                }
                else {
                    opResult = getClient().create().creatingParentsIfNeeded().forPath(nodeName, value.getBytes(Charsets.UTF_8));
                }
                suc = Objects.equal(nodeName, opResult);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return suc;
    }

    /**
     * 删除 node
     *
     * @param nodeName
     */
    public void deleteNode(String nodeName) {
        try {
            getClient().delete().deletingChildrenIfNeeded().forPath(nodeName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新 node
     *
     * @param nodeName
     * @param value
     * @return
     */
    public boolean updateNode(String nodeName, String value) {
        boolean suc = false;
        try {
            Stat stat = getClient().checkExists().forPath(nodeName);
            if (stat != null) {
                Stat opResult = getClient().setData().forPath(nodeName, value.getBytes(Charsets.UTF_8));
                suc = opResult != null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return suc;
    }

    /**
     * 创建 | 更新 node
     *
     * @param nodeName
     * @param value
     * @return
     */
    public boolean creatUpdateNode(String nodeName, String value) {
        boolean suc = false;
        try {
            Stat stat = getClient().checkExists().forPath(nodeName);
            if (stat == null) {
                String opResult = null;
                if (Strings.isNullOrEmpty(value)) {
                    opResult = getClient().create().creatingParentsIfNeeded().forPath(nodeName);
                }
                else {
                    opResult = getClient().create().creatingParentsIfNeeded().forPath(nodeName, value.getBytes(Charsets.UTF_8));
                }
                suc = Objects.equal(nodeName, opResult);
            } else {
                Stat opResult = getClient().setData().forPath(nodeName, value.getBytes(Charsets.UTF_8));
                suc = opResult != null;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return suc;
    }


    // ---------------------- child node ----------------------

    /**
     * 找到指定节点下所有子节点的名称与值
     *
     * @param node
     * @return
     */
    public Map<String, String> listChildrenDetail(String node) {
        Map<String, String> map = Maps.newHashMap();
        try {
            GetChildrenBuilder childrenBuilder = getClient().getChildren();
            List<String> children = childrenBuilder.forPath(node);
            GetDataBuilder dataBuilder = getClient().getData();
            if (children != null) {
                for (String child : children) {
                    String propPath = ZKPaths.makePath(node, child);
                    map.put(child, new String(dataBuilder.forPath(propPath), Charsets.UTF_8));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 列出子节点的名称
     *
     * @param node
     * @return
     */
    public List<String> listChildren(String node) {
        List<String> children = Lists.newArrayList();
        try {
            GetChildrenBuilder childrenBuilder = getClient().getChildren();
            children = childrenBuilder.forPath(node);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return children;
    }


    // ---------------------- watch ----------------------

    /**
     * 增加监听
     *
     * @param node
     * @param isSelf
     *            true 为node本身增加监听 false 为node的子节点增加监听
     * @throws Exception
     */
    public void addWatch(String node, boolean isSelf) throws Exception {
        if (isSelf) {
            getClient().getData().watched().forPath(node);
        }
        else {
            getClient().getChildren().watched().forPath(node);
        }
    }


    /**
     * 增加监听
     *
     * @param node
     * @param isSelf
     *            true 为node本身增加监听 false 为node的子节点增加监听
     * @param watcher
     * @throws Exception
     */
    public void addWatch(String node, boolean isSelf, Watcher watcher) throws Exception {
        if (isSelf) {
            getClient().getData().usingWatcher(watcher).forPath(node);      // 仅仅能监控指定的本节点的数据修改,删除 操作 并且只能监听一次 --->不好
        }
        else {
            getClient().getChildren().usingWatcher(watcher).forPath(node);
        }
    }


    /**
     * 增加监听
     *
     * @param node
     * @param isSelf
     *            true 为node本身增加监听 false 为node的子节点增加监听
     * @param watcher
     * @throws Exception
     */
    public void addCuratorWatcher(String node, boolean isSelf, CuratorWatcher watcher) throws Exception {
        if (isSelf) {
            getClient().getData().usingWatcher(watcher).forPath(node);
        }
        else {
            getClient().getChildren().usingWatcher(watcher).forPath(node);
        }
    }

    public void addCuratorListener(CuratorListener curatorListener) {
        getClient().getCuratorListenable().addListener(curatorListener);

    }


    // ---------------------- test ----------------------

    public static void main(String[] args) throws Exception {

        String zkAddress = "127.0.0.1:2181";
        String namespace = "xxl-conf";

        CuratorClient curator = new CuratorClient(zkAddress, namespace);

        curator.addCuratorListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                System.out.println(event.toString() + ".......................");
                final WatchedEvent watchedEvent = event.getWatchedEvent();
                if (watchedEvent != null) {
                    System.out.println(watchedEvent.getState() + "=========== listener ============" + watchedEvent.getType());
                    if (watchedEvent.getState() == KeeperState.SyncConnected) {
                        switch (watchedEvent.getType()) {
                            case NodeChildrenChanged:
                                // TODO
                                break;
                            case NodeDataChanged:
                                // TODO
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        });


        curator.createNode("/test1", "你好abc11");
        curator.createNode("/test2", "你好abc22");
        curator.updateNode("/test2", "你好abc2222");
        curator.creatUpdateNode("/test3", "你好abc33");
        curator.creatUpdateNode("/test3", "你好abc3333");

        List<String> list = curator.listChildren("/");
        Map<String, String> map = curator.listChildrenDetail("/");

        // curator.deleteNode("/xxl-conf");
        // curator.destory();

        System.out.println("=========================================");
        System.out.println(list);

        System.out.println("=========================================");
        System.out.println(map);

        // 增加监听
        curator.addWatch("/test1", false);

        TimeUnit.SECONDS.sleep(600);
    }


}

