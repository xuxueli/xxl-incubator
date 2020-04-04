package com.xxl.glue.admin.controller.resolver;

import com.xxl.glue.admin.core.exception.WebException;
import com.xxl.glue.admin.core.result.ReturnT;
import com.xxl.glue.admin.core.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		
		// 异常封装
		ReturnT<String> result = new ReturnT<String>(ReturnT.FAIL_CODE, null);
		if (ex instanceof WebException) {
			result.setCode(((WebException) ex).getCode());
			result.setMsg(((WebException) ex).getMsg());
		} else {
			result.setCode(500);
			result.setMsg(ex.toString().replaceAll("\n", "<br/>"));
			logger.info("system catch exception:{}", ex);
		}
				
		// 是否JSON返回
		HandlerMethod method = (HandlerMethod)handler;
		ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
		if (responseBody != null) {
			mv.addObject("result", JacksonUtil.writeValueAsString(result));
			mv.setViewName("/common/common.result");
		} else {
			mv.addObject("exceptionMsg", result.getMsg());	
			mv.setViewName("/common/common.exception");
		}
		return mv;
	}

	
}