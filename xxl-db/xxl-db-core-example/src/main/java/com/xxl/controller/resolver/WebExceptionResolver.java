package com.xxl.controller.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.xxl.core.exception.WebException;
import com.xxl.core.result.ReturnT;

/**
 * 异常解析器
 * @author xuxueli
 */
public class WebExceptionResolver implements HandlerExceptionResolver {
	private static transient Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mv = new ModelAndView();
		
		ReturnT<String> result = new ReturnT<String>();
		if (ex instanceof WebException) {
			result.setCode(((WebException) ex).getCode());
			result.setMsg(((WebException) ex).getMsg());
			logger.info("system catch exception:{}", ex);
		} else {
			result.setCode(500);
			result.setMsg(ex.toString().replaceAll("\n", "<br/>"));
			logger.info("system catch exception:{}", ex);
		}
				
		return mv;
	}

	
}