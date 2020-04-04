package com.xxl.hex.handler.response;

/**
 * response msg
 * @author xuxueli 2015-11-16 21:09:14
 */
public abstract class HexResponse {
	public transient static final int CODE_SUCCESS = 200;
	public transient static final int CODE_FAIL = 500;

	private int code;
	private String msg;

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

	/**
	 * simple HexResponse
     */
	public static class SimpleHexResponse extends HexResponse{

		public SimpleHexResponse() {
		}
		public SimpleHexResponse(String msg) {
			super.code = CODE_FAIL;
			super.msg = msg;
		}
		public SimpleHexResponse(int code, String msg) {
			super.code = code;
			super.msg = msg;
		}
	}

}
