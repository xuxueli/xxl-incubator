package com.xxl.util.core.skill.jgroups;

import org.jgroups.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JgroupsNode extends ReceiverAdapter  {
	private static Logger logger = LoggerFactory.getLogger(JgroupsNode.class);
	private static final String GROUP_NAME = "default_group";	// //群组名，一个JGroups群的唯一标示符。
	private static final String GROUP_CONF = "jgroups/tcp-7800.xml";
	
	// -------------------------- channel init --------------------------
	private JChannel jChannel;
	public void init() {
		if (jChannel == null) {
			try {
				logger.info(">>>>>>>>>>> jgroup jChannel init ...");
				jChannel = new JChannel(GROUP_CONF); 
				jChannel.setDiscardOwnMessages(false); // true:不接自己发送的消息;false：接收
				jChannel.setReceiver(this);
				jChannel.connect(GROUP_NAME);	// join jroup
			} catch (Exception e) {
				e.printStackTrace();
				if (jChannel!=null) {
					jChannel.close();
				}
			}
			// jChannel.close();
		}
	}
	
	public JChannel channel() {
		if (jChannel == null) {
			init();
		}
		return jChannel;
	}
	
	// -------------------------- message send --------------------------
	public void send(Object msg) throws Exception{
		/**
		 * // 第一个参数是接收方的地址，如果是null的话，表示是广//播，即群里所有接收方都会收到消息。收到消息
		 * // 第二个参数是发送方地址。
		 */
		Message message = new Message(null, channel().getAddress(), msg);
		channel().send(message);
	}
	
	// -------------------------- message receive --------------------------
	@Override
	public void receive(Message msg) {
		//Address src = msg.getSrc();
		//Address dest = msg.dest();
		logger.info(">>>>>>>>>>> message receive:{}", msg.getObject());
	}

	@Override
	public void getState(OutputStream output) throws Exception {
		logger.info(">>>>>>>>>>> getState:{}", output);
	}

	@Override
	public void suspect(Address mbr) {
		logger.info(">>>>>>>>>>> suspect:{}", mbr);
	}

	@Override
	public void viewAccepted(View new_view) {
		logger.info(">>>>>>>>>>> new_view:{}", new_view);
		List<Address> addresses = new_view.getMembers();
		for (Address address : addresses) {
			logger.info(">>>>>>>>>>> new_view.address:{}", address.toString());
		}
	}

	public static void main(String[] args) throws Exception {
		JgroupsNode node = new JgroupsNode();
		
		int index = 1;
		int NO = new Random().nextInt(99);
		while (true) {
			TimeUnit.SECONDS.sleep(3);
			String msg = NO + "：你好，index-" + (index++) ;
			logger.info("发送消息：" + msg);
			node.send(msg);
		}
		
	}
}
