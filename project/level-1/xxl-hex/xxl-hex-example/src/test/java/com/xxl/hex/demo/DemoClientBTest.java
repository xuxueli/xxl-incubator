package com.xxl.hex.demo;

import com.xxl.hex.remote.client.HexClient;
import com.xxl.hex.serialise.JacksonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * JSON方式调用: 本方式适用于Client同为Java语言情况;
 *
 * 调用方法: 使用官网提供 "HexClient.handleJson" 方法发起API请求
 * 特点: 使用灵活, 不需要依赖Server端
 */
public class DemoClientBTest {

	private static final String BASE_URL = "http://localhost:8080/hex";
	private static final String mapping = "default/demohandler.wapi";

	public static void main(String[] args) throws Exception {

		// 第一步: "JSON格式-API请求" 初始化 (key值和服务端请求参数属性值保持一致)
		Map<String, Object> requestMap = new HashMap<String, Object>();
		requestMap.put("passphrase", "qwerasdf");
		requestMap.put("a", 1);
		requestMap.put("b", 2);
		String request_json = JacksonUtil.writeValueAsString(requestMap);

		// invoke
		String response_json = HexClient.handleJson(BASE_URL, mapping, request_json);

		// 第er步: Finish, 可以获取API响应数据, 开发自己的业务了
		System.out.println("json = " + response_json);

	}

}