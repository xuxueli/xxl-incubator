package com.xxl.glue.core.broadcast.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * xxl zk client
 *
 * 1. reconnect on loss
 * 2. heartbeat detection
 *
 * @author xuxueli 2015-8-28 10:37:43
 */
public abstract class XxlZkClient implements Watcher {
	private static final Logger logger = LoggerFactory.getLogger(XxlZkClient.class);

	// ------------------------------ contra ------------------------------
	private String zkserver;
	public XxlZkClient(String zkserver){
		this.zkserver = zkserver;
	}

	// ------------------------------ zookeeper ------------------------------
	private ZooKeeper zookeeper;
	private ReentrantLock INSTANCE_INIT_LOCK = new ReentrantLock(true);

	/**
	 * get or make zookeeper conn
	 *
	 * @return
	 */
	protected ZooKeeper getClient(){
		if (zookeeper != null) {
			return zookeeper;	// just return
		} else {
			try {
				if (INSTANCE_INIT_LOCK.tryLock(2, TimeUnit.SECONDS)) {
					if (zookeeper != null) {
						return zookeeper;	// unlock(finally) + return
					}
					zookeeper = new ZooKeeper(zkserver, 10000, this);
					logger.info(">>>>>>>>> XxlZkClient.zookeeper connnect success.");
				}
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			} finally {
				INSTANCE_INIT_LOCK.unlock();
			}
		}
		return zookeeper;
	}

	/**
	 * close zookeeper conn
	 */
	protected void close(){
		if (zookeeper != null) {
			try {
				zookeeper.close();
				zookeeper = null;
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * exists or creat node
	 *
	 * @param path
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	protected Stat existsOrCreat(String path) throws KeeperException, InterruptedException {
		// valid path
		if (path==null || path.length()<1) {
			return null;
		}

		// exist or create path
		Stat stat = zookeeper.exists(path, false);
		if (stat != null) {
			return stat;
		} else {
			// make parent (/../../..)
			String parentPath = path.substring(0, path.lastIndexOf("/"));
			if (parentPath.length() < path.length()) {
				existsOrCreat(parentPath);
			}
			// make path
			zookeeper.create(path, new byte[]{}, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);	// TODO ? if PERSISTENT
			return null;
		}

	}

	/**
	 * set data for node
	 *
	 * @param path
	 * @param data
	 * @return
	 */
	protected Stat setData(String path, String data) {
		try {
			Stat stat = existsOrCreat(path);
			Stat ret = getClient().setData(path, data.getBytes(), stat!=null?stat.getVersion():-1);
			return ret;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

}

