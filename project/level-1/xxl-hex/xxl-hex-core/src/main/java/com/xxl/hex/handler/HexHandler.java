
package com.xxl.hex.handler;

import com.xxl.hex.handler.request.HexRequest;
import com.xxl.hex.handler.response.HexResponse;

/**
 * request handler
 * @author xuxueli 
 * @version 2015-11-28 13:56:05
 */
public abstract class HexHandler<T extends HexRequest> {

	/**
	 * do some validate
	 * @param request
	 * @return
     */
	public abstract HexResponse validate(T request);

	/**
	 * invoke biz handle
	 * @param request
	 * @return
     */
	public abstract HexResponse handle(T request);
	
}
