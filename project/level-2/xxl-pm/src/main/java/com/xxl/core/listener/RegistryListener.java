package com.xxl.core.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.xxl.service.ResourceBundle;

/**
 * 注册监听器
 * @author xuxueli
 */
public class RegistryListener implements ServletContextListener {
	private static transient Logger logger = LoggerFactory.getLogger(RegistryListener.class);
	
	public void contextDestroyed(ServletContextEvent context) {
		
	}

	public void contextInitialized(ServletContextEvent sc) {
		logger.info("[5i net registry listener starting...]");
		
		ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(sc.getServletContext());
		
		// 资源实例
		ResourceBundle resource = ResourceBundle.getInstance();
		resource.setFreemarkerConfig((FreeMarkerConfig) context.getBean("freemarkerConfig"));
		
		logger.info("[5i net registry listener finished...]");
	}

}
