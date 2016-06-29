package com.xxl.util.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.util.core.util.HtmlTemplateUtil;
import com.xxl.util.core.util.HttpClientUtil;
import com.xxl.util.core.util.JacksonUtil;
import com.xxl.util.core.util.PropertiesUtil;
import com.xxl.util.core.util.WebPathUtil;

/**
 * UTIL
 * @author xuxueli 2016-6-29 18:59:43
 */
@Controller
public class IndexController {
	
	@RequestMapping("/HtmlTemplateUtil")
	@ResponseBody
	public String HtmlTemplateUtil(){
		// generate String
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("WebPathUtil", HtmlTemplateUtil.generateStaticModel(WebPathUtil.class.getName()));
		
		String result = HtmlTemplateUtil.generateString(params, "freemarker-test.ftl");
		
		// generate Html File
		HtmlTemplateUtil.generateFile(params, "freemarker-test.ftl", "freemarker-test.html");
		
		return result;
	}
	
	@RequestMapping("/HttpClientUtil")
	@ResponseBody
	public String HttpClientUtil(){
		String result = HttpClientUtil.post("https://www.hao123.com/", null);
		return result;
	}
	
	@RequestMapping("/JacksonUtil")
	@ResponseBody
	public String JacksonUtil(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("aaa", "111");
		map.put("bbb", "222");
		
		String result = JacksonUtil.writeValueAsString(map);
		return result;
	}
	
	@RequestMapping("/PropertiesUtil")
	@ResponseBody
	public String PropertiesUtil(){
		Properties prop = PropertiesUtil.loadProperties("config.properties");
		
		String result = PropertiesUtil.getString(prop, "name");
		return result;
	}
	
	
}
