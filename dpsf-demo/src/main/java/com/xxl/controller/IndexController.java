package com.xxl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.service.IDpsfService;

@Controller
public class IndexController {
	
	@Autowired
	private IDpsfService dpsfService;
	
	@Autowired
	private IDpsfService peginDpsfService;
	
	@RequestMapping("")
	@ResponseBody
	public String index(String name){
		return dpsfService.sayHi(name);
	}

	@RequestMapping("/pegin")
	@ResponseBody
	public String pegin(String name){
		return peginDpsfService.sayHi(name);
	}
	
}
