package com.xxl.core.exception;

/**
 * 自定义异常
 * @author xuxueli 2015-12-12 18:04:30
 */
public class WebException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public int code;
	public String msg;
	
	public WebException() {
	}
	public WebException(String msg) {
		this.code = 500;
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
}