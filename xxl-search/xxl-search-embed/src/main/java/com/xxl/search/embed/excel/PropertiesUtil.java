package com.xxl.search.embed.excel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Properties;

/**
 * properties util
 * @author xuxueli 2015-6-22 22:36:46
 */
public class PropertiesUtil {
	private static Logger logger = LogManager.getLogger();
	public static final String DEFAULT_CONFIG = "search-field.properties";
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
			if (url != null) {
				in = new FileInputStream(url.getPath());
				// in = loder.getResourceAsStream(propertyFileName); // 方式2：配置更新需重启JVM
				if (in != null) {
					prop.load(in);
				}
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
	 * load prop file, from disk path
	 * @param propertyFileName
	 * @return
     */
	public static Properties loadFileProperties(String propertyFileName) {
		Properties prop = new Properties();
		InputStream in = null;
		try {
			ClassLoader loder = Thread.currentThread().getContextClassLoader();
			URL url = url = new File(propertyFileName).toURI().toURL();
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
	 * load prop value of string
	 * @param key
	 * @return
	 */
	public static String getString(Properties prop, String key) {
		return prop.getProperty(key);
	}


	public static final String KEYWORDS = "keywords";
	public static LinkedHashSet<String> getLinkedSet(Properties prop, String key) {
		String vals = getString(prop, key);
		if (vals==null || vals.trim().length()==0) {
			throw new IllegalArgumentException("搜索Field不可为空");
		}
		LinkedHashSet<String> fields = new LinkedHashSet<>(Arrays.asList(vals.split(",")));
		if (!fields.contains(PropertiesUtil.KEYWORDS)) {
			throw new IllegalArgumentException("搜索Field必须包含参数keywords");
		}
		return fields;
	}

	public static void main(String[] args) {
		Properties prop = loadProperties(DEFAULT_CONFIG);
		System.out.println(getLinkedSet(prop, "search-field"));
	}

}
