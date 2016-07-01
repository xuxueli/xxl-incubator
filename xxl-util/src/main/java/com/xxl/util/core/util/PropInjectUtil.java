package com.xxl.util.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties auto inject util
 * @author xuxueli 2015-5-4 22:01:22
 */
public class PropInjectUtil {
	private static transient Logger logger = LoggerFactory.getLogger(PropInjectUtil.class);
	private static final String PROP_NAME = "config.properties";
	
	static {
		init();
	}
	public static void reFresh() {
		init();
	}
	private static void init() {
		
		Properties prop = PropertiesUtil.loadProperties(PROP_NAME);
		if (prop == null) {
			logger.warn("prop file[{}] load fail.", PROP_NAME);
			return;
		}
		
		Field[] allFields = PropInjectUtil.class.getDeclaredFields();
		for (Field field : allFields) {
			if (Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers())) {
				field.setAccessible(true);
				
				Class<?> clazz = field.getType();
				String value = prop.getProperty(field.getName());
				if (clazz == String.class) {
					try {
						field.set(PropInjectUtil.class, value);
					} catch (IllegalArgumentException e) {
						logger.error("[puic activity new year init field value illegal argument exception.]", e);
					} catch (IllegalAccessException e) {
						logger.error("[puic activity new year init field value illegal access exception.]", e);
					}
				} else if (clazz == Integer.TYPE) {
					try {
						field.set(PropInjectUtil.class, Integer.parseInt(value));
					} catch (IllegalArgumentException e) {
						logger.error("[puic activity new year init field value illegal argument exception.]", e);
					} catch (IllegalAccessException e) {
						logger.error("[puic activity new year init field value illegal access exception.]", e);
					}
				}
			}			
		}
	}
	
	public static String name;
	
	public static void main(String[] args) {
		System.out.println(name);
	}
	
}
