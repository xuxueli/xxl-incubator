package com.xxl.service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.xxl.core.result.ReturnT;

/**
 * 用户信息
 * @author xuxueli
 */
public interface IUserService {

	/**
	 * 登陆
	 * @param email
	 * @param password
	 * @return
	 */
	ReturnT<String> login(HttpServletResponse response, HttpSession session, String email, String password);
	
}
