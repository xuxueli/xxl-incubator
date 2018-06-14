package com.xxl.util.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * properties util
 * @author xuxueli 2015-6-22 22:36:46
 */
public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	public static final String DEFAULT_CONFIG = "config.properties";
	/**
	 * load prop file
	 * @param propertyFileName
	 * @return
	 */
	public static Properties loadProperties(String propertyFileName) {
		Properties prop = new Properties();
		InputStream in = null;
		try {

			// 方式1：配置更新不需要重启JVM		（springboot 项目可能有问题）
			ClassLoader loder = Thread.currentThread().getContextClassLoader();
			URL url = loder.getResource(propertyFileName);
			if (url != null) {
				in = new FileInputStream(url.getPath());
			}

			// 方式2：配置更新需重启JVM
			// in = loder.getResourceAsStream(propertyFileName);
			if (in != null) {
				prop.load(in);
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
		return prop;
	}

	/**
	 * load prop file, from disk path
	 * @param propertyFileName
	 * @return
     */
	public static Properties loadFileProperties(String propertyFileName) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			ClassLoader loder = Thread.currentThread().getContextClassLoader();
			URL url = new File(propertyFileName).toURI().toURL();
			in = new FileInputStream(url.getPath());
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
	 * load prop value of string
	 * @param key
	 * @return
	 */
	public static String getString(Properties prop, String key) {
		return prop.getProperty(key);
	}

	/**
	 * load prop value of int
	 * @param key
	 * @return
	 */
	public static int getInt(Properties prop, String key) {
		return Integer.parseInt(getString(prop, key));
	}

	/**
	 * load prop value of boolean
	 * @param prop
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(Properties prop, String key) {
		return Boolean.valueOf(getString(prop, key));
	}

	public static void main(String[] args) {
		Properties prop = loadProperties(DEFAULT_CONFIG);
		System.out.println(getString(prop, "name"));
	}

}
