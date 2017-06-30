package com.xxl.controller.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

/**
 * 博客拦截器
 * @author xuxueli
 */
public class BlogInterceptor implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(BlogInterceptor.class);
	
	@Override
	public void intercept(Invocation inv) {
		logger.info("================Before invoking:{}", inv.getActionKey());
		inv.invoke();
		logger.info("================After invoking:{}", inv.getActionKey());
		
	}

}
