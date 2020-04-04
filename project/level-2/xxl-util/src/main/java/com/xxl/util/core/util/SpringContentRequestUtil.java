package com.xxl.util.core.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * spring content util, by HttpServletRequest
 * @author xuxueli 2016-7-6 14:51:21
 */
public class SpringContentRequestUtil {

	public static ApplicationContext getSpringContent(HttpServletRequest request){
		ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		return applicationContext;
	}
	
	/**
	 * load bean by name in spring
	 * @param name
	 * @return
	 */
	public static Object getBeanByName(HttpServletRequest request, String name){
		Object bean = getSpringContent(request).getBean(name);
		return bean;
	}
	
	/**
	 * load bean by type in spring
	 * @param type
	 * @return
	 */
	public static Object getBeanByType(HttpServletRequest request, Class<?> type){
		Object bean = getSpringContent(request).getBean(type);
		return bean;
	}
	
}
