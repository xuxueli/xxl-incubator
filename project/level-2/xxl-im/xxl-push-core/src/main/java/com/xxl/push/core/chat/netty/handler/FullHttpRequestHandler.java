package com.xxl.push.core.chat.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * Websocket Handshake：http请求报文中包含websocket相关的信息，服务器端可对请求进行鉴权 
 * @author xuxueli 2016-5-27 18:01:49
 */
public class FullHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	String chatUrl;
	public FullHttpRequestHandler(String chatUrl) {
		this.chatUrl = chatUrl;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

		if(chatUrl.equalsIgnoreCase(msg.uri())){
			// 鉴权
			// ...
			
			// Http协议升级WebSocket协议：释放资源，资源转发下个handler
			ctx.fireChannelRead(msg.retain());	
			return;
		} else {
			// 非法HTTP请求，响应404 
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
			response.setStatus(HttpResponseStatus.NOT_FOUND);
			ctx.writeAndFlush(response);
		}
        
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace(System.err);
	}
	
}
