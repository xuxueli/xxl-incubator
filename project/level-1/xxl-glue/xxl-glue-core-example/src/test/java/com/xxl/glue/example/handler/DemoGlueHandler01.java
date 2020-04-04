package com.xxl.glue.example.handler;

import com.xxl.glue.core.handler.GlueHandler;

import java.util.HashSet;
import java.util.Map;

/**
 * 示例场景01：托管 “配置信息”
 *
 * 优点：
 * 		1、在线编辑；推送更新；
 * 		2、该场景下，相较于同类型配置管理系统，支持数据类型更加丰富，不仅支持基础类型，甚至支持复杂对象；
 * 		3、该场景下，配置信息的操作和展示，更加直观；
 *
 * @author xuxueli 2016-4-14 15:36:37
 */
public class DemoGlueHandler01 implements GlueHandler {

	@Override
	public Object handle(Map<String, Object> params) {
		
		/*
		// 【基础类型配置】，例如：活动开关、短信发送次数阀值、redis地址等；
		boolean activitySwitch = true;													// 活动开关：true=开、false=关
		int smsLimitCount = 3;															// 短信发送次数阀值
		String brokerURL = "failover:(tcp://127.0.0.1:61616,tcp://127.0.0.2:61616)";	// redis地址等

		// 【对象类型配置……】
		*/

		// 【列表配置】
		HashSet<String> blackTelephones = new HashSet<String>();						// 手机号码黑名单列表
		blackTelephones.add("15000000000");
		blackTelephones.add("15000000001");
		blackTelephones.add("15000000002");

		return blackTelephones;
	}
	
}
