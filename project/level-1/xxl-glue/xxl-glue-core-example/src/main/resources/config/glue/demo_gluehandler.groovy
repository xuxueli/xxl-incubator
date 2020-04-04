package com.xxl.groovy.example.service.impl

import com.xxl.glue.core.handler.GlueHandler
/**
 * 场景A：托管 “配置信息” ，尤其适用于数据结构比较复杂的配置项
 * 优点：在线编辑；推送更新；+ 直观；
 * @author xuxueli 2016-4-14 15:36:37
 */
public class DemoHandlerAImplLocal implements GlueHandler {

	@Override
	public Object handle(Map<String, Object> params) {

		// 【基础类型配置】
		boolean ifOpen = true;															// 开关
		int smsLimitCount = 3;															// 短信发送次数阀值
		String brokerURL = "failover:(tcp://127.0.0.1:61616,tcp://127.0.0.2:61616)";	// 套接字配置

		// 【列表配置】
		Set<Integer> blackShops = new HashSet<Integer>();								// 黑名单列表
		blackShops.add(15826714);
		blackShops.add(15826715);
		blackShops.add(15826716);
		blackShops.add(15826717);
		blackShops.add(15826718);
		blackShops.add(15826719);

		// 【KV配置】
		Map<Integer, String> emailDispatch = new HashMap<Integer, String>();			// 不同BU标题文案配置
		emailDispatch.put(555, "淘宝");
		emailDispatch.put(666, "天猫");
		emailDispatch.put(777, "聚划算");

		// 【复杂集合配置】
		Map<Integer, List<Integer>> openCitys = new HashMap<Integer, List<Integer>>();	// 不同城市推荐商户配置
		openCitys.put(11, Arrays.asList(15826714, 15826715));
		openCitys.put(22, Arrays.asList(15826714, 15651231, 86451231));
		openCitys.put(33, Arrays.asList(48612323, 15826715));

		return smsLimitCount;
	}

}
