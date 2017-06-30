package com.xxl.core.result;

import com.xxl.core.result.ReturnT;

/**
 * 封装返回
 * @author xuxueli 2015-3-29 18:27:32
 * @param <T>
 */
public class ReturnT<T> {
	public static final ReturnT<String> SUCCESS = new ReturnT<String>(200, null);
	public static final ReturnT<String> FAIL = new ReturnT<String>(500, null);
	
	private int code;
	private String msg;
	private T returnContent;
	
	public ReturnT() {
		this.code = 200;
	}
	public ReturnT(T returnContent) {
		this.code = 200;
		this.returnContent = returnContent;
	}
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getReturnContent() {
		return returnContent;
	}
	public void setReturnContent(T returnContent) {
		this.returnContent = returnContent;
	}
	
}