package com.xxl.dao;

import java.util.List;

import com.xxl.core.model.main.UserMain;

/**
 * 用户信息
 * @author xuxueli
 */
public interface IUserMainDao {

	/**
	 * 查询
	 * @param email		: 邮箱
	 * @param encrypt	: 密码 (md5加密)
	 * @return
	 */
	public UserMain getByEmailPwd(String email, String password);

	public UserMain getByUserId(int userId);

	public List<UserMain> getByRole(String role);

}
