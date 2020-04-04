package com.xxl.glue.example.handler;

import com.xxl.glue.core.handler.GlueHandler;

import java.util.Map;

/**
 * 示例场景03：托管 “动态服务”
 *
 * 优点：
 * 		1、在线编辑；推送更新；
 * 		2、该场景下，服务内部可以灵活组装和调用其他Service服务, 扩展服务的动态特性，作为公共服务。
 *
 * @author xuxueli 2016-4-14 16:07:03
 */
public class DemoGlueHandler03 implements GlueHandler {
	private static final String SHOPID = "shopid";
	
	/*
	@Resource
	private UserPhoneService userPhoneService;	// 手机号码黑名单Service，此处仅作为示例
	*/
	
	/**
	 * 商户黑名单判断
	 */
	@Override
	public Object handle(Map<String, Object> params) {
		
		/*
		String telephone = (params!=null)? (String) params.get("telephone") :null;

		boolean isBlackTelephone = userPhoneService.isBlackTelephone(telephone);
		return isBlackTelephone;
		*/
		
		return true;
	}
	
}
