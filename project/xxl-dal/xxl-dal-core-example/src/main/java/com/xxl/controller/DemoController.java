package com.xxl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.core.model.XxlDbDemo;
import com.xxl.core.result.ReturnT;
import com.xxl.service.IDemoService;

@Controller
public class DemoController {
	
	@Autowired
	private IDemoService demeService;
	
	@RequestMapping("")
	@ResponseBody
	public ReturnT<String> index() {
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping("/load")
	@ResponseBody
	public ReturnT<XxlDbDemo> load(int id) {
		XxlDbDemo xxlDbDemo = demeService.load(id);
		return new ReturnT<XxlDbDemo>(xxlDbDemo);
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<Integer> load(int id, int age) {
		XxlDbDemo xxlDbDemo = new XxlDbDemo();
		xxlDbDemo.setId(id);
		xxlDbDemo.setAge(age);
		int ret = demeService.update(xxlDbDemo);
		return new ReturnT<Integer>(ret);
	}
	
}
