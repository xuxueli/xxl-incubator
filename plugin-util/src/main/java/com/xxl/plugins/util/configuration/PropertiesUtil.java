package com.xxl.plugins.util.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties util
 * @author xuxueli 2015-6-22 22:36:46
 */
public class PropertiesUtil {
	protected static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	/**
	 * load prop file
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperties(String propertyFileName) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			ClassLoader loder = Thread.currentThread().getContextClassLoader();
			URL url = loder.getResource(propertyFileName); // 方式1：配置更新不需要重启JVM
			in = new FileInputStream(url.getPath());
			// in = loder.getResourceAsStream(propertyFileName); // 方式2：配置更新需重启JVM
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
	 * load prop value of string
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getString(Properties prop, String key) {
		return prop.getProperty(key);
	}

	/**
	 * load prop value of int
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	protected static int getInt(Properties prop, String key) {
		return Integer.parseInt(getString(prop, key));
	}
	
	public static void main(String[] args) {
		Properties prop = loadProperties("config.properties");
		System.out.println(getString(prop, "name"));
	}

}
