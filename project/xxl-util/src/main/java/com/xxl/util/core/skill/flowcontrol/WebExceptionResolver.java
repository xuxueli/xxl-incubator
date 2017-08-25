package com.xxl.util.core.skill.flowcontrol;

import com.xxl.util.core.util.JacksonUtil;
import com.xxl.util.core.util.MvcUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 异常解析器
 *
 *
 <code>
 	<!-- 异常解析 -->
	 <bean id="exceptionResolver" class="com.dianping.wed.common.mvc.resolver.WebExceptionResolver" >
		 <!-- 异常jsonp兼容 -->
		 <property name="jsonpParam">
			 <list>
				 <value>jsonp</value>
				 <value>callback</value>
			 </list>
		 </property>
		 <!-- 异常页面ftl模板 -->
		 <property name="errorTemplate" value="/common/common.exception" />
	 </bean>
 </code>
 * @author xuxueli 2016-17-07
 */
public class WebExceptionResolver implements HandlerExceptionResolver {
	private static transient Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

	private List<String> jsonpParam;
	private String errorTemplate;

	public void setJsonpParam(List<String> jsonpParam) {
		this.jsonpParam = jsonpParam;
	}
	public List<String> getJsonpParam() {
		return jsonpParam;
	}
	public String getErrorTemplate() {
		return errorTemplate;
	}
	public void setErrorTemplate(String errorTemplate) {
		this.errorTemplate = errorTemplate;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
										 HttpServletResponse response, Object handler, Exception ex) {
		logger.error(ex.getMessage(), ex);
		ModelAndView mv = new ModelAndView();

		boolean isJson = MvcUtil.isJsonHandler(handler);
		if (isJson) {

			ReturnT<String> result = new ReturnT<String>(null);
			if (ex instanceof WebException) {
				result.setCode(((WebException) ex).getCode());
				result.setMsg(((WebException) ex).getMsg());
			} else {
				result.setCode(500);
				result.setMsg(ex.toString().replaceAll("\n", "<br/>"));
			}

			/*response.setContentType("application/json;charset=utf-8");
			mv.addObject("result", JacksonUtil.writeValueAsString(result));
			mv.setViewName("/common/common.result");*/

			try {
				// json data
				String resultVal = JacksonUtil.writeValueAsString(result);

				// jsonp support
				String jsonp = request.getParameter("jsonp");
				if (jsonpParam != null) {
					for (int i = 0; i < jsonpParam.size(); i++) {
						jsonp = request.getParameter(jsonpParam.get(i));
						if (jsonp!=null && jsonp.trim().length()>0) {
							break;
						}
					}
				}
				if (jsonp!=null && jsonp.trim().length()>0) {
					resultVal = jsonp + "("+ resultVal +")";
				}

				response.setContentType("application/json;charset=utf-8");
				response.getWriter().print(resultVal);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		} else {

			String exceptionMsg = null;
			if (ex instanceof WebException) {
				exceptionMsg = ((WebException) ex).getMsg();
			} else {
				exceptionMsg = ex.toString().replaceAll("\n", "<br/>");
			}

			mv.addObject("exceptionMsg", exceptionMsg);
			mv.setViewName(errorTemplate);
		}

		return mv;
	}


}