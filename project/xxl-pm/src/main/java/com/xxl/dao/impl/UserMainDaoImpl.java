package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.UserMain;
import com.xxl.dao.IUserMainDao;

/**
 * 用户信息
 * @author xuxueli
 */
@Repository
public class UserMainDaoImpl implements IUserMainDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	
	/*
	 * 查询
	 * @see com.xxl.dao.IUserMainDao#getByEmailPwd(java.lang.String, java.lang.String)
	 */
	@Override
	public UserMain getByEmailPwd(String email, String password){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("email", email);
		params.put("password", password);
		return sqlSessionTemplate.selectOne("UserMainMapper.getByEmailPwd", params);
	}

	@Override
	public UserMain getByUserId(int userId) {
		return sqlSessionTemplate.selectOne("UserMainMapper.getByUserId", userId);
	}

	@Override
	public List<UserMain> getByRole(String role) {
		return sqlSessionTemplate.selectList("UserMainMapper.getByRole", role);
	}

}
