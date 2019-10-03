package com.xxl.web.core.response;


import com.xxl.web.core.response.annotation.XxlWebContentType;

/**
 * response msg
 * @author xuxueli 2015-11-16 21:09:14
 */
public abstract class XxlWebResponse {

	public abstract XxlWebContentType contentType();

	public abstract String content() throws Exception;

}
