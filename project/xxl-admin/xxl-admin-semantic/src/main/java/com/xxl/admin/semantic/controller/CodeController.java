package com.xxl.admin.semantic.controller;

import com.xxl.admin.semantic.controller.annotation.PermessionLimit;
import com.xxl.admin.semantic.core.result.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/code")
public class CodeController {
	

	@RequestMapping
	@PermessionLimit
	public String index(){
		return "code/code.list";
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	@PermessionLimit
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length, String name){


		int list_count = 100;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 100; i++) {
			if (i >= start && i<(start+length)) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", i);
				map.put("name", "名称" + i);
				map.put("about", "介绍如下" + i);
				list.add(map);
			}
		}


		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recordsTotal", list_count);		// 总记录数
		map.put("recordsFiltered", list_count);		// 过滤后的总记录数
		map.put("data", list);  					// 数据

		return map;
	}
	
	@RequestMapping("/delCode")
	@ResponseBody
	@PermessionLimit
	public ReturnT<String> delCode(int id){
		return ReturnT.SUCCESS;
	}
	
	@PermessionLimit
	@RequestMapping("/addCode")
	@ResponseBody
	public ReturnT<String> saveCode(){
		return ReturnT.SUCCESS;
	}
	

}
