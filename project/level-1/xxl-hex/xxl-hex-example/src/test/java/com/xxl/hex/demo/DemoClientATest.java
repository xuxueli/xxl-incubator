package com.xxl.hex.demo;

import com.xxl.hex.demo.msg.DemoRequest;
import com.xxl.hex.demo.msg.DemoResponse;
import com.xxl.hex.remote.client.HexClient;
import com.xxl.hex.serialise.JacksonUtil;

/**
 * 对象方式调用: 本方式适用于Client同为Java语言, 且Client端可以获取Server端API接口对应的的Request和Response的Java文件 的情况;
 *
 * 调用方法: 使用官方提供 "HexClient.handleObj" 方法发起API请求
 * 特点: 使用方便, 但是需要依赖Server端API接口的Request和Response的Java类文件;
 *
 */
public class DemoClientATest {

	private static final String BASE_URL = "http://localhost:8080/hex";
	private static final String mapping = "default/demohandler.wapi";
	
	public static void main(String[] args) throws Exception {

		// 封装参数
		DemoRequest demoRequest = new DemoRequest();
		demoRequest.setPassphrase("qwerasdf");
		demoRequest.setA(1);
		demoRequest.setB(2);

		// invoke
		DemoResponse demoResponse = (DemoResponse) HexClient.handleObj(BASE_URL, mapping, demoRequest, DemoResponse.class);

		System.out.println("json=" + JacksonUtil.writeValueAsString(demoResponse));

	}

}
