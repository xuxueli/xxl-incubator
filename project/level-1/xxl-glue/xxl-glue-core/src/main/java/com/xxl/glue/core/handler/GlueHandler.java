package com.xxl.glue.core.handler;

import java.util.Map;

/**
 * default glue iface, it could be use in your biz service
 * @author xuxueli 2016-1-2 21:31:56
 */
public interface GlueHandler {
	
	/**
	 * defaule method
	 * @param params
	 * @return
	 */
	public Object handle(Map<String, Object> params);
	
}
