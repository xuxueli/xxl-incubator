package com.xxl.util.core.util;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.*;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * API：
 *
 *      ".delete().inBackground()"=异步删除；
 *      ".create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath"=创建临时节点
 *
 * @author xuxueli 2017-08-23 20:23:17
 */
public class CuratorClient {
    private static Logger logger = LoggerFactory.getLogger(CuratorClient.class);

    // ---------------------- client ----------------------

    private CuratorFramework client;

    /**
     * @param connectString
     *      like "ip:2181" or "ip01:2181,ip02:2181,ip03:2181"
     * @param newNamespace
     *      use specified namespace "/xxl-conf" or null
     */
    public CuratorClient(String connectString, String newNamespace) {
        // build client
        client = CuratorFrameworkFactory.builder().
                connectString(connectString).
                namespace(newNamespace).
                sessionTimeoutMs(30 * 1000).
                connectionTimeoutMs(15 * 1000).
                retryPolicy(new ExponentialBackoffRetry(1000, 3)).
                build();
        client.start();

        // listener event
        addCuratorListener(new CuratorListener() {
            @Override
            public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
                logger.warn(">>>>>>>>>>>> xxl-conf curator listener, CuratorEvent={}, ", event.toString());
                final WatchedEvent watchedEvent = event.getWatchedEvent();
                if (watchedEvent != null) {
                    if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
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
                // 创建 node
                String optResult = null;
                if (Strings.isNullOrEmpty(value)) {
                    optResult = getClient().create().creatingParentsIfNeeded().forPath(nodeName);    // PERSISTENT ： 默认永久节点 (.withMode(CreateMode.EPHEMERAL_SEQUENTIAL)，可设置临时节点、永久节点)
                }
                else {
                    optResult = getClient().create().creatingParentsIfNeeded().forPath(nodeName, value.getBytes(Charsets.UTF_8));
                }
                suc = Objects.equal(nodeName, optResult);
            } else {
                // 更新 node
                Stat opResult = getClient().setData().forPath(nodeName, value.getBytes(Charsets.UTF_8));
                suc = opResult != null;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
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
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 获取数据 node
     *
     * @param nodeName
     * @return
     */
    public String getNoteData(String nodeName){
        try {

            Stat stat = getClient().checkExists().forPath(nodeName);
            if (stat != null) {
                //byte[] bytes = getClient().getData().watched().inBackground().forPath(nodeName);
                byte[] bytes = getClient().getData().watched().forPath(nodeName);
                if (bytes != null) {
                    return new String(bytes, Charsets.UTF_8);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    // ---------------------- child node ----------------------

    /**
     * 查询子节点，名称
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
            logger.error(e.getMessage(), e);
        }
        return children;
    }

    /**
     * 查询子节点，名称与值
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
            logger.error(e.getMessage(), e);
        }
        return map;
    }


    // ---------------------- watch ----------------------

    /**
     * 增加监听 （一次性，默认watch）
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
     * 增加监听  （一次性，本节点的数据修改、删除）
     *
     * @param node
     * @param isSelf
     *            true 为node本身增加监听 false 为node的子节点增加监听
     * @param watcher
     * @throws Exception
     */
    public void addWatcher(String node, boolean isSelf, Watcher watcher) throws Exception {
        if (isSelf) {
            getClient().getData().usingWatcher(watcher).forPath(node);
            //getClient().getData().usingWatcher(watcher).inBackground().forPath(node); // 异步监听
        }
        else {
            getClient().getChildren().usingWatcher(watcher).forPath(node);
        }
    }


    /**
     * 增加监听 （一次性，本节点的数据修改、删除）
     *
     * @param node
     * @param isSelf
     *            true 为node本身增加监听 false 为node的子节点增加监听
     * @param watcher
     * @throws Exception
     */
    public void addCuratorWatcher(String node, boolean isSelf, CuratorWatcher watcher) throws Exception {
        if (isSelf) {
            getClient().getData().usingWatcher(watcher).forPath(node);      // 仅仅能监控指定的本节点的数据修改,删除 操作 并且只能监听一次 --->不好
        }
        else {
            getClient().getChildren().usingWatcher(watcher).forPath(node);
        }
    }

    /**
     * 增加监听 （一次性，本节点的数据修改、删除）
     *
     * @param curatorListener
     */
    public void addCuratorListener(CuratorListener curatorListener) {
        getClient().getCuratorListenable().addListener(curatorListener);    // 一次性的监听操作,使用后就无法在继续监听了
    }


    // ---------------------- watch 2 ----------------------

    /**
     * 增加监听
     *
     *      范围：当前节点-新增、更新、数据更新
     *      注意：
     *          start() + close()，务必对应；
     *          不监听删除；
     *          删除节点后会再次创建(空节点)；
     *          删除节点后，重新创建，依然监听
     *          频繁操作，可会会发生时间丢失；
     *          一个watch
     *
     * @param node
     * @param compressed
     * @param listener
     * @return
     */
    public NodeCache addNodeCacheListener(String node, boolean compressed, NodeCacheListener listener) {
        try {
            NodeCache nodeCache = new NodeCache(getClient(), node, compressed);
            nodeCache.getListenable().addListener(listener);
            nodeCache.start();
            return nodeCache;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 增加监听
     *
     *      范围：子节点-新增、更新、数据更新
     *      注意：
     *          start() + close()，务必对应
     *          不会监听子节点的子节点
     *          频繁操作，频繁操作会事件丢失；
     *          cacheDate：true时，event.getData().getData()为空；false时，频繁操作会事件丢失，导致数据不准确；
     *          一个watch
     *
     * @param node
     * @param cacheData
     * @param listener
     */
    public PathChildrenCache addPathChildrenCacheListener(String node, boolean cacheData, PathChildrenCacheListener listener){
        try {
            PathChildrenCache childrenCache = new PathChildrenCache(getClient(), node, cacheData);
            childrenCache.getListenable().addListener(listener);
            childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            return childrenCache;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 增加监听
     *
     *      范围：当前节点 + 子节点 + 孙子节点等
     *      注意：
     *          start() + close()，务必对应
     *          可以进行本节点的删除(不在创建))
     *          子孙节点删除后创建，依然会监听
     *          频繁操作，监听不会丢失
     *          操作一个子节点，新增一个watch
     *
     * @param node
     * @param treeCacheListener
     * @return
     */
    public TreeCache addTreeCache (String node, TreeCacheListener treeCacheListener) {
        try {
            TreeCache treeCache = new TreeCache(getClient(), node);
            treeCache.getListenable().addListener(treeCacheListener);
            treeCache.start();
            return treeCache;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    // ---------------------- test ----------------------

    public static void main(String[] args) throws Exception {

        // create
        String zkAddress = "127.0.0.1:2181";
        String namespace = "xxl-conf";
        String testPath = "/test1";

        final CuratorClient curator = new CuratorClient(zkAddress, namespace);

        int testFalg = 3;
        if (testFalg == 1) {
            // node opt
            curator.deleteNode("/");
            curator.creatUpdateNode(testPath, "你好abc1");
            curator.creatUpdateNode(testPath, "你好abc1111");

            String nodeData = curator.getNoteData(testPath);
            logger.info("nodeData=" + nodeData);

            List<String> list = curator.listChildren("/");
            logger.info("list=" + list);

            Map<String, String> map = curator.listChildrenDetail("/");
            logger.info("map=" + list);
        } else if (testFalg == 2) {
            // watch
            curator.addWatch(testPath, false);

            curator.addWatcher(testPath, true, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    logger.info("node is changed: " + event.toString());
                }
            });
        } else if (testFalg == 3) {

            // node cache :   监控本节点的变化情况
            final NodeCache nodeCache = new NodeCache(curator.getClient(), testPath, false);

            NodeCacheListener nodeCacheListener = new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {

                    String path = null;
                    String data = null;
                    Stat stat = null;
                    if (nodeCache.getCurrentData() != null) {
                        path = nodeCache.getCurrentData().getPath();
                        data = new String(nodeCache.getCurrentData().getData(), Charsets.UTF_8);
                        stat = nodeCache.getCurrentData().getStat();
                    }

                    logger.info(">>>>>>>>>>> node cache, path={}, data={}, stat={}", path, data, stat);
                }
            };

            nodeCache.getListenable().addListener(nodeCacheListener);
            nodeCache.start();

            curator.creatUpdateNode(testPath, "aaa");
            //TimeUnit.MILLISECONDS.sleep(100);
            curator.deleteNode(testPath);
            curator.creatUpdateNode(testPath, "bbb");
            //TimeUnit.MILLISECONDS.sleep(100);

            for (int i = 0; i < 20; i++) {
                curator.creatUpdateNode(testPath, "val-"+i);
                //TimeUnit.MILLISECONDS.sleep(100);
            }

            TimeUnit.MINUTES.sleep(5);

        } else if (testFalg == 4) {
            // path cache : 子节点-新增、更新、数据更新
            PathChildrenCacheListener childrenCacheListener = new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                    PathChildrenCacheEvent.Type childType = event.getType();    // CHILD_ADDED、CHILD_REMOVED、CHILD_UPDATED
                    ChildData childDataObj = event.getData();


                    String childPath = null;
                    String childData = null;
                    String quertData = null;
                    if (childDataObj != null) {
                        childPath = childDataObj.getPath();
                        if (childDataObj.getData() != null) {
                            childData = new String(childDataObj.getData(), Charsets.UTF_8);
                        }
                        quertData = curator.getNoteData(childPath);
                    }

                    logger.info(">>>>>>>>>>> path cache, event={}, childType={}, childPath={}, childData={}, queryData={}",
                            event, childType, childPath, childData, quertData);
                }
            };
            PathChildrenCache pathChildrenCache = curator.addPathChildrenCacheListener("/", false, childrenCacheListener);

            curator.creatUpdateNode(testPath, "aaa");
            TimeUnit.MILLISECONDS.sleep(100);
            curator.deleteNode(testPath);
            curator.creatUpdateNode(testPath, "bbb");
            TimeUnit.MILLISECONDS.sleep(100);

            for (int i = 0; i < 10; i++) {
                curator.creatUpdateNode(testPath, "val-"+i);
                TimeUnit.MILLISECONDS.sleep(100);
            }

            TimeUnit.MINUTES.sleep(5);

        } else if (testFalg == 5) {

            // tree cache 当前节点 + 子节点 + 孙子节点等
            TreeCacheListener treeCacheListener = new TreeCacheListener() {
                @Override
                public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                    TreeCacheEvent.Type treeType = event.getType();     // NODE_ADDED、NODE_REMOVED、NODE_UPDATED、、
                    ChildData childData = event.getData();
                    String path = null;
                    String data =  null;
                    if (childData != null) {
                        path = childData.getPath();
                        data = new String(childData.getData(), Charsets.UTF_8);
                    }

                    logger.info(">>>>>>>>>>> tree cache, event={}, treeType={}, childData={}, path={}, data={}",
                            event, treeType, childData, path, data);

                }
            };
            TreeCache treeCache = curator.addTreeCache("/", treeCacheListener);

            curator.creatUpdateNode(testPath, "111");
            //TimeUnit.MILLISECONDS.sleep(100);

            curator.creatUpdateNode(testPath+"/222", "222");
            //TimeUnit.MILLISECONDS.sleep(100);

            curator.deleteNode("/");

            curator.creatUpdateNode(testPath+"/222/333", "333");
            //TimeUnit.MILLISECONDS.sleep(100);

        }

        TimeUnit.MINUTES.sleep(5);
        curator.destory();
    }


}

