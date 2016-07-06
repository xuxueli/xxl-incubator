package com.xxl.util.core.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring content listener by listener
 * @author xuxueli
 */
public class SpringContentListener implements ServletContextListener {
	private static transient Logger logger = LoggerFactory.getLogger(SpringContentListener.class);
	
	private static ApplicationContext applicationContext;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("SpringContentListener init start...");
		applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		logger.info("SpringContentListener init success...");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * load bean by name in spring
	 * @param name
	 * @return
	 */
	public static Object getBeanByName(String name){
		Object bean = SpringContentListener.applicationContext.getBean(name);
		return bean;
	}
	
	/**
	 * load bean by type in spring
	 * @param type
	 * @return
	 */
	public static Object getBeanByType(Class<?> type){
		Object bean = SpringContentListener.applicationContext.getBean(type);
		return bean;
	}
	
}
