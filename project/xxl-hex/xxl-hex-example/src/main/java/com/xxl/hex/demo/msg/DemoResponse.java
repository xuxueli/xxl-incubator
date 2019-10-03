package com.xxl.hex.demo.msg;

import com.xxl.hex.handler.response.HexResponse;

import java.io.Serializable;

/**
 * 	开发HexResponse流程:
 *
 * 		1、需要继承HexResponse父类
 * 		2、需要实现Serializable接口
 *
 */
public class DemoResponse extends HexResponse implements Serializable {
	private static final long serialVersionUID = 42L;

	private int sum;

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}
}
