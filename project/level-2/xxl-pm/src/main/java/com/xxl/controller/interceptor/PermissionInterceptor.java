package com.xxl.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.controller.core.BaseLoginController;
import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.exception.WebException;
import com.xxl.service.helper.LoginIdentityHelper;

/**
 * “登陆+权限”拦截器
 * @author xuxueli
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	//private static transient Logger logger = LoggerFactory.getLogger(LoginPermissionInterceptor.class);
	
	/*
	 * Controller“执行时异步”“执行
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#afterConcurrentHandlingStarted(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		// TODO Auto-generated method stub
		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	/*
	 * Controller“执行后”执行
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
	}

	/*
	 * Controller“执行前”执行
	 * @see org.springframework.web.servlet.handler.HandlerInterceptorAdapter#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		
		// 请求非法
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}
		
		// 获取“权限注解”
		HandlerMethod method = (HandlerMethod)handler;
		PermessionType permission = method.getMethodAnnotation(PermessionType.class);
		if (permission == null) {
			throw new WebException(ReturnCodeEnum.FAIL.code(), "权限受限");
		}
		boolean loginState = permission.loginState();
		
		// 01：登陆拦截
		if (loginState) {
			LoginIdentity identity = LoginIdentityHelper.cacheCheck(request, session);
			if (identity == null) {
				//response.sendRedirect(request.getContextPath() + "/loginCheck.do");
				//return false;
				throw new WebException(ReturnCodeEnum.FAIL.code(), "登录失效,请重新登录");
			}
			if (method.getBean() instanceof BaseLoginController) {
				BaseLoginController baseLoginController = (BaseLoginController) method.getBean();
				baseLoginController.setLoginIdentity(identity);
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	
}
