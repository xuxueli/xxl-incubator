package com.xxl.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

/**
 * Properties工具类
 * 
 * @author xuxueli
 */
public class PropertiesUtil {
	protected static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	/**
	 * 加载Properties
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperties(String propertyFileName) {
		Properties prop = new Properties();
		InputStreamReader  in = null;
		try {
			ClassLoader loder = Thread.currentThread().getContextClassLoader();
			
			// 方式1：配置更新需重启JVM
			//in = new InputStreamReader(loder.getResourceAsStream(propertyFileName), "UTF-8");;
			
			// 方式2：配置更新不需要重启JVM
			URL url = loder.getResource(propertyFileName); 
			in = new InputStreamReader(new FileInputStream(url.getPath()), "UTF-8");
			
			if (in != null) {
				prop.load(in);
			}
		} catch (IOException e) {
			logger.error("load {} error!", propertyFileName);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("close {} error!", propertyFileName);
				}
			}
		}
		return prop;
	}

	/**
	 * 加载Prop
	 *
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperties2(String propertyFileName) {
		Properties prop = null;
		try {
			Resource resource = new ClassPathResource(propertyFileName);
			EncodedResource encodedResource = new EncodedResource(resource,"UTF-8");
			prop = PropertiesLoaderUtils.loadProperties(encodedResource);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return prop;
	}

	/**
	 * 获取配置String
	 *
	 * @param prop
	 * @param key
	 * @return
	 */
	public static String getString(Properties prop, String key) {
		return prop.getProperty(key);
	}

	/**
	 * 获取配置int
	 *
	 * @param prop
	 * @param key
	 * @return
	 */
	protected static int getInt(Properties prop, String key) {
		return Integer.parseInt(getString(prop, key));
	}

	
	public static void main(String[] args) {
		Properties prop = loadProperties("mail.properties");
		System.out.println(getString(prop, "mail.sendNick"));
	}

}
