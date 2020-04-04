package com.xxl.glue.example.controller;


import com.xxl.glue.core.GlueFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * demo controller
 * @author xuxueli 2016-1-23 16:58:31
 */
@Controller
public class IndexController {
	
	@RequestMapping
	@ResponseBody
	public String index() {
		try {
			String telephone = "15000000000";

			StringBuffer sb = new StringBuffer();
			sb.append("valid black telephone: ").append(telephone).append("<hr><br>")
			.append("DemoGlueHandler01: ").append(isBlackTelephone01(telephone)).append("<br><br>")
			.append("DemoGlueHandler02: ").append(isBlackTelephone02(telephone)).append("<br><br>")
			.append("DemoGlueHandler03: ").append(isBlackTelephone03(telephone));

			return sb.toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	@RequestMapping("/glue")
	@ResponseBody
	public String glue(String name) {
		try {
			Object result = GlueFactory.glue(name, null);

			return result.toString();
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * 示例场景01：托管 “配置信息”
	 *
	 * @param telephone
	 * @return
	 * @throws Exception
	 */
	private boolean isBlackTelephone01(String telephone) throws Exception {
		Set<String> blackTelephones = (Set<String>) GlueFactory.glue("demo_project.DemoGlueHandler01", null);
		if (blackTelephones.contains(telephone)) {
			return true;
		}
		return false;
	}

	/**
	 * 示例场景02：托管 “静态方法”
	 *
	 * @param telephone
	 * @return
	 * @throws Exception
	 */
	private boolean isBlackTelephone02(String telephone) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("telephone", telephone);

		Boolean result = (Boolean) GlueFactory.glue("demo_project.DemoGlueHandler02", params);
		return result;
	}

	/**
	 * 示例场景03：托管 “动态服务”
	 *
	 * @param telephone
	 * @return
	 * @throws Exception
	 */
	private boolean isBlackTelephone03(String telephone) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("telephone", "15000000000");

		Boolean result = (Boolean) GlueFactory.glue("demo_project.DemoGlueHandler03", params);
		return result;
	}

}
