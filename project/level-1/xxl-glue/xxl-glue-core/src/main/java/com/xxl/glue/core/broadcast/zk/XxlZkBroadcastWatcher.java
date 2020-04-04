package com.xxl.glue.core.broadcast.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * xxl zk broadcast watcher
 *
 * @author xuxueli 2015-10-29 14:43:46
 */
public abstract class XxlZkBroadcastWatcher extends XxlZkClient implements Watcher {
	private static final Logger logger = LoggerFactory.getLogger(XxlZkBroadcastWatcher.class);

	public XxlZkBroadcastWatcher(String zkserver) {
		super(zkserver);
	}

	@Override
	public void process(WatchedEvent event) {

		// reconnect on loss (session expired)
		if (event.getState() == Event.KeeperState.Expired) {
			super.close();
			super.getClient();
		}

		// node data changed
		if (event.getType() == Event.EventType.NodeDataChanged){
			String path = event.getPath();

			/**
			 * add one-time watch
			 *
			 * 很有意义：因为我们需要的是最新的数据，并不是实时的。one-time可以保证【Watch事件，重新Watch】时间段内不会重复监听响应，但是可保证getData是最新的，会通过临时Watch校验中间是否有新数据。
			 */
			try {
				super.getClient().exists(path, true);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}


			String data = null;
			try {
				byte[] resultData = super.getClient().getData(path, true, null);
				data = new String(resultData);
			} catch (KeeperException e) {
				logger.error(e.getMessage(), e);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}

			consume(path, data);
		}

	}

	/**
	 * produce msg (set data for node)
	 *
	 * @param path
	 * @param data
	 * @return
	 */
	protected boolean produce(String path, String data) {
		Stat stat = setData(path, data);
		boolean ret = stat!=null?true:false;
		logger.info(">>>>>>>>>>> XxlZkBroadcastWatcher producer:{}, {}={}", ret, path, data);
		return ret;
	}

	/**
	 * watch broadcast topic (watch node)
	 *
	 * @param path
	 * @return
	 */
	protected boolean watchTopic(String path) {
		try {
			Stat stat = super.getClient().exists(path, true);
			if (stat == null) {
				stat = super.existsOrCreat(path);
				stat = super.getClient().exists(path, true);
			}
			boolean ret = stat!=null?true:false;
			logger.info(">>>>>>>>>>> XxlZkBroadcastWatcher watchTopic:{}, path:{}", ret, path);
			return ret;
		} catch (KeeperException e) {
			logger.error(e.getMessage(), e);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * consume msg (watch node)
	 *
	 * @param path
	 * @param data
	 */
	protected void consume(String path, String data) {
		try {
			consumeMsg(path, data);
			logger.info(">>>>>>>>>>> XxlZkBroadcastWatcher consume:{}={}", path, data);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	public abstract void consumeMsg(String path, String data) throws Exception;
}