package com.xxl.push.core.chat.socketio;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.xxl.push.core.chat.socketio.listener.SioChatListener;

/**
 * socketio server
 * @author xuxueli 
 */
public class SioChatServer {

	public int port = 7000;
	public void setPort(int port) {
		this.port = port;
	}

	SocketIOServer server;
	public void init(){
		Configuration config = new Configuration();
		config.setHostname("127.0.0.1");
		config.setPort(port);

		server = new SocketIOServer(config);
		server.addListeners(new SioChatListener(server));

		server.start();
	}

	public void destroy(){
		if (server!=null) {
			server.stop();
		}
	}

}
