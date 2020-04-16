package com.xxl.push.core.chat.netty;

import com.xxl.push.core.chat.netty.handler.FullHttpRequestHandler;
import com.xxl.push.core.chat.netty.handler.TextWebSocketFrameHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * netty chat server
 * @author xuxueli
 */
public class NettyChatServer {
	private static Logger logger = LoggerFactory.getLogger(NettyChatServer.class);
	
	public int port = 7000;
	public String chatUrl = "/chat";
	public void setPort(int port) {
		this.port = port;
	}
	public void setChatUrl(String chatUrl) {
		this.chatUrl = chatUrl;
	}

	public void init(){
		Thread chatThread = new Thread(new Runnable() {
			@Override
			public void run() {
				
		        EventLoopGroup bossgroup = new NioEventLoopGroup();
		        EventLoopGroup workergroup = new NioEventLoopGroup();
		        try {
		        	ServerBootstrap boot = new ServerBootstrap();
		            boot.group(bossgroup, workergroup)
		            	.channel(NioServerSocketChannel.class)
	            		.childHandler(new ChannelInitializer<Channel>() {
							@Override
							protected void initChannel(Channel ch) throws Exception {
								ChannelPipeline pipeline = ch.pipeline();
								
								pipeline.addLast(new HttpServerCodec());						// 将请求响应消息编解码成HTTP消息
								pipeline.addLast(new HttpObjectAggregator(65536));				// 将HTTP消息的多个部分组合成一条完整的HTTP消息
								pipeline.addLast(new ChunkedWriteHandler());					// 用于支持浏览器和服务端进行websocket通信z
								pipeline.addLast(new WebSocketServerProtocolHandler(chatUrl));	// websocketPath
								pipeline.addLast(new FullHttpRequestHandler(chatUrl));			// FullHttpRequest for http Handshake
								pipeline.addLast(new TextWebSocketFrameHandler());				// TextWebSocketFrame for text websocket data
							}
						});
		            Channel ch = boot.bind(port).sync().channel();
		            logger.info(">>>>>>>>>>> Netty Chat Server as port[{}] Running ...", port);
		            ch.closeFuture().sync();
		        } catch (Exception e) {
		            e.printStackTrace();
		        } finally {
		            bossgroup.shutdownGracefully();
		            workergroup.shutdownGracefully();
		        }
		        
			}
		});
		chatThread.setDaemon(true);
		chatThread.start();
		
	}

	public void destroy(){
		// TODO
	}
	
}
