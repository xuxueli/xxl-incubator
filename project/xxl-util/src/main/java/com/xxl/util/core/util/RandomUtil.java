package com.xxl.util.core.util;

import java.util.Random;

/**
 * 随机字符串生成，用户随机/短信验证码生成等
 * @author xuxueli 2015-5-5 17:50:58
 */
public class RandomUtil {
	
	public static final String NUMBERS = "0123456789";
	public static final String LOWER_CHARS = "abcdefghijklmnopqrstuvwxyz";
	public static final String UPPER_CHARS = LOWER_CHARS.toUpperCase();
	
	/**
	 * 随机字符串
	 * @param length
	 */
	public static String randomStr(String dic, int len) {
		if (dic==null || dic.trim().length()==0 || len<1) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		Random rd = new Random();
		for (int i = 0; i < len; i++) {
			result.append( dic.charAt(Math.abs(rd.nextInt()) % dic.length()) );
		}
		return result.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(randomStr(NUMBERS, 6));
		System.out.println(randomStr(LOWER_CHARS, 6));
		System.out.println(randomStr(UPPER_CHARS, 6));
	}

}
