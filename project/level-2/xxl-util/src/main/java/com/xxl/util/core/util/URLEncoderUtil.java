package com.xxl.util.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * URL编码解码
 * @author xuxueli 2015-5-16 19:27:50
 */
public final class URLEncoderUtil {
	private static Logger logger = LoggerFactory.getLogger(URLEncoderUtil.class);
	
	/**
	 * URLDecode编码 (utf-8)
	 * @param param
	 */
	public static String encode(String param){
		try {
			return URLEncoder.encode(param, "utf-8");
		} catch (Exception e) {
			logger.error("encode error:", e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * URLDecode解码 (utf-8)
	 * @param param
	 */
	public static String decode(String param){
		try {
			return URLDecoder.decode(param, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		String url = "https://www.baidu.com/正能量";
		System.out.println(url);
		
		// encode
		String encodeUrl = encode(null);
		System.out.println(encodeUrl);
		
		// decode
		String decodeUrl = decode(encodeUrl);
		System.out.println(decodeUrl);
		
	}
	
}
