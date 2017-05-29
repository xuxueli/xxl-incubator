package com.xxl.admin.semantic.controller;

import com.xxl.admin.semantic.controller.annotation.PermessionLimit;
import com.xxl.admin.semantic.controller.interceptor.PermissionInterceptor;
import com.xxl.admin.semantic.core.result.ReturnT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class IndexController {


	@RequestMapping("")
	@PermessionLimit(login=false)
	public String index(Model model, HttpServletRequest request) {
		if (!PermissionInterceptor.ifLogin(request)) {
			return "redirect:/toLogin";
		}
		return "redirect:/code";
	}

	@RequestMapping("/toLogin")
	@PermessionLimit(login=false)
	public String toLogin(Model model, HttpServletRequest request) {
		if (PermissionInterceptor.ifLogin(request)) {
			return "redirect:/";
		}
		return "login";
	}

	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(login=false)
	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String ifRemember, String userName, String password){
		// param
		boolean ifRem = false;
		if (ifRemember!=null && "on".equals(ifRemember)) {
			ifRem = true;
		}

		if (!("admin".equals(userName) && "123456".equals(password))) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "登陆失败");
		}
		PermissionInterceptor.login(response, ifRem);
		return ReturnT.SUCCESS;
	}

	@RequestMapping(value="logout", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(login=false)
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
		PermissionInterceptor.logout(request, response);
		return ReturnT.SUCCESS;
	}

	@RequestMapping("/help")
	@PermessionLimit
	public String index(){
		return "help";
	}
	
}
