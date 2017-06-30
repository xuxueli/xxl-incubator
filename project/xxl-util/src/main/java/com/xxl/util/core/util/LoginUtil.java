package com.xxl.util.core.util;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 分布式，登陆验证器，cookie + memcached实现
 * @author xuxueli 2015-12-12 18:09:04
 */
public class LoginUtil {
	
	private static final String LOGIN_IDENTITY_KEY = "LOGIN_IDENTITY";
	
	/**
	 * 服务端，用户登陆信息，分布式缓存key
	 * @param userid
	 * @return
	 */
	private static String cacheKey (int userid){
		return LOGIN_IDENTITY_KEY.concat("_").concat(String.valueOf(userid));
	}
	
	/**
	 * 客户端，用户登陆标记，cookie缓存value，格式化
	 * @param loginIdentity
	 * @return
	 */
	private static String cookieValue(LoginIdentity loginIdentity){
		return String.valueOf(loginIdentity.getUserid()).concat("_").concat(loginIdentity.getUsertoken());
	}
	
	/**
	 * 客户端，用户登陆标记，cookie缓存value，解析
	 * @param cookieValue
	 * @return
	 */
	private static LoginIdentity cookieValueParse(String cookieValue){
		String[] temp = cookieValue.split("_");
		LoginIdentity loginIdentity = new LoginIdentity();
		loginIdentity.setUserid(Integer.valueOf(temp[0]));
		loginIdentity.setUsertoken(temp[1]);
		return loginIdentity;
	}
	
	/**
	 * 登陆成功触发的逻辑
	 * @param response
	 * @param loginIdentity
	 * @return
	 */
	public static boolean login(HttpServletResponse response, LoginIdentity loginIdentity){
		XMemcachedUtil.set(cacheKey(loginIdentity.getUserid()), loginIdentity);
		CookieUtil.set(response, LOGIN_IDENTITY_KEY, cookieValue(loginIdentity), true);
		return true;
	}
	
	/**
	 * 注销登陆触发的逻辑
	 * @param request
	 * @param response
	 * @param userid
	 */
	public static void logout(HttpServletRequest request, HttpServletResponse response, int userid){
		XMemcachedUtil.delete(cacheKey(userid));
		CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
	}
	
	/**
	 * 登陆状态监测
	 * @param request
	 * @return
	 */
	public static boolean loginCheck(HttpServletRequest request){
		String cookieValue = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
		LoginIdentity cookieLoginIdentity = cookieValueParse(cookieValue);
		LoginIdentity cacheLoginIdentity = (LoginIdentity) XMemcachedUtil.get(cacheKey(cookieLoginIdentity.getUserid()));
		if (cookieLoginIdentity!=null && cacheLoginIdentity!=null 
				&& cookieLoginIdentity.getUsertoken().equals(cacheLoginIdentity.getUsertoken())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 用户缓存登陆信息
	 * @author xuxueli 2016-7-5 19:59:23
	 */
	public static class LoginIdentity implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private int userid;
		private String username;
		private String password;
		private String usertoken;
		
		public int getUserid() {
			return userid;
		}
		public void setUserid(int userid) {
			this.userid = userid;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getUsertoken() {
			return usertoken;
		}
		public void setUsertoken(String usertoken) {
			this.usertoken = usertoken;
		}
	}
	
}
