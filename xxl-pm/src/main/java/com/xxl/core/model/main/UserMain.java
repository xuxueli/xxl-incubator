package com.xxl.core.model.main;

import java.io.Serializable;

/**
 * 用户信息
 * @author xuxueli
 */
@SuppressWarnings("serial")
public class UserMain implements Serializable {
	
	private int userId;
	private String email;
	private String password;
	private String realName;
	private String role;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
