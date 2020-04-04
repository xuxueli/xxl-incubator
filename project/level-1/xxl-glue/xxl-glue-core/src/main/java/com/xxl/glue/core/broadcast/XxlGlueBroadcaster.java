package com.xxl.glue.core.broadcast;

import com.xxl.glue.core.GlueFactory;
import com.xxl.glue.core.broadcast.zk.XxlZkBroadcastWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * xxl-glue broadcast
 *
 * @author xuxueli 2015-10-29 14:43:46
 */
public class XxlGlueBroadcaster extends XxlZkBroadcastWatcher {
	private static final Logger logger = LoggerFactory.getLogger(XxlGlueBroadcaster.class);

	private static XxlGlueBroadcaster instance = null;
	public static XxlGlueBroadcaster getInstance() {
		return instance;
	}

	public XxlGlueBroadcaster(String zkserver) {
		super(zkserver);
		instance = this;
	}

	/**
	 * broadcast topic : /xxl-glue/glueKey
	 */
	private static final String GLUE_BASE = "/xxl-glue";
	private static final String GLUE_BROADCAST = GLUE_BASE + "/broadcast";

	/**
	 * procuce msg
	 * @param glueName
	 * @param appnames
	 * @param version
	 * @return
	 */
	public boolean procuceMsg(String glueName, Set<String> appnames, long version) {
		String topicPath = GLUE_BROADCAST + "/" + glueName;

		GlueMessage message = new GlueMessage();
		message.setGlueName(glueName);
		message.setAppnames(appnames);
		message.setVersion(version);
		String data = JacksonUtil.writeValueAsString(message);

		return super.produce(topicPath, data);
	}

	/**
	 * watch msg
	 *
	 * @param name
	 * @return
	 */
	public boolean watchMsg(String name){
		String topic = GLUE_BROADCAST + "/" + name;

		return watchTopic(topic);
	}

	/**
	 * consume msg
	 *
	 * @param path
	 * @param data
	 */
	@Override
	public void consumeMsg(String path, String data) {
		GlueMessage glueMessage = JacksonUtil.readValue(data, GlueMessage.class);
		GlueFactory.glueRefresh(glueMessage);
	}

}