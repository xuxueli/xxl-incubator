package com.xxl.hex.demo.msg;

import com.xxl.hex.handler.request.HexRequest;

/**
 * 开发Request流程: 创建普通Java类即可
 */
public class DemoRequest extends HexRequest {

	// 推荐根据业务需求, 定制一个公共Request对象, 然后在业务Handler的validate方式中, 调用公共校验service完成对公共Request对象的校验工作
	private String passphrase;

	private int a;
	private int b;

	public String getPassphrase() {
		return passphrase;
	}

	public void setPassphrase(String passphrase) {
		this.passphrase = passphrase;
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	@Override
	public String toString() {
		return "DemoRequest{" +
				"passphrase='" + passphrase + '\'' +
				", a=" + a +
				", b=" + b +
				'}';
	}
}
