package com.xxl.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.controller.core.LoginIdentity;
import com.xxl.core.result.ReturnT;
import com.xxl.service.IUserService;
import com.xxl.service.helper.LoginIdentityHelper;

/**
 * 登陆相关
 * @author xuxueli
 */
@Controller
public class LoginController {
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping("")
	@PermessionType(loginState=false)
	public String index(){
		return "front/index";
	}
	
	@RequestMapping("/login")
	@ResponseBody
	@PermessionType(loginState=false)
	public ReturnT<String> login(HttpServletResponse response, HttpSession session, String email, String password){
		return userService.login(response, session, email, password);
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	@PermessionType
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		LoginIdentityHelper.logout(request, response, session);
		return new ReturnT<String>();
	}
	
	@RequestMapping("/loginCheck")
	@ResponseBody
	@PermessionType(loginState=true)
	public ReturnT<LoginIdentity> loginCheck(HttpServletRequest request, HttpSession session){
		LoginIdentity loginIdentity = LoginIdentityHelper.cacheCheck(request, session);
		return new ReturnT<LoginIdentity>(loginIdentity);
	}
	
}
