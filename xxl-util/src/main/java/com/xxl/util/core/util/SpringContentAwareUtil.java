package com.xxl.util.core.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring content util, by ApplicationContextAware
 * @author xuxueli 2016-1-15 16:05:42
 */
public class SpringContentAwareUtil implements ApplicationContextAware {
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContentAwareUtil.applicationContext = applicationContext;
	}
	
	/**
	 * load bean by name in spring
	 * @param name
	 * @return
	 */
	public static Object getBeanByName(String name){
		Object bean = SpringContentAwareUtil.applicationContext.getBean(name);
		return bean;
	}
	
	/**
	 * load bean by type in spring
	 * @param type
	 * @return
	 */
	public static Object getBeanByType(Class<?> type){
		Object bean = SpringContentAwareUtil.applicationContext.getBean(type);
		return bean;
	}
	
}
