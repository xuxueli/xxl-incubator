package com.xxl.glue.example.handler;

import com.xxl.glue.core.handler.GlueHandler;

import java.util.HashSet;
import java.util.Map;

/**
 * 示例场景02：托管 “静态方法”
 *
 * 优点：
 * 		1、在线编辑；推送更新；
 * 		2、该场景下，托管公共组件，方便组件统一维护和升级；
 *
 * @author xuxueli 2016-4-14 16:07:03
 */
public class DemoGlueHandler02 implements GlueHandler {

	// 手机号码黑名单列表
	private static HashSet<String> blackTelephones = new HashSet<String>();
	static {
		blackTelephones.add("15000000000");
		blackTelephones.add("15000000001");
		blackTelephones.add("15000000002");
	}

	/**
	 * 手机号码黑名单校验Util
	 *
	 * @param telephone
	 * @return
	 */
	private boolean isBlackTelephone(String telephone) {
		if (telephone!=null && blackTelephones.contains(telephone)) {
			return true;
		}
		return false;
	}
	
	@Override
	public Object handle(Map<String, Object> params) {
		String telephone = (params!=null)? (String) params.get("telephone") :null;
		return isBlackTelephone(telephone);
	}
	
}
