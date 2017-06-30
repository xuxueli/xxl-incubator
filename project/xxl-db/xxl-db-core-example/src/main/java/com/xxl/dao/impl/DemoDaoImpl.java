package com.xxl.dao.impl;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import com.xxl.core.model.XxlDbDemo;
import com.xxl.dao.IDemoDao;
import com.xxl.db.router.XxlDbRouter;
import com.xxl.db.router.XxlDbRouter.DB_TYPE;

@Component
public class DemoDaoImpl implements IDemoDao {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public int save(XxlDbDemo xxlDbDemo) {
		return sqlSessionTemplate.insert("XxlDbDemoMapper.save", xxlDbDemo);
	}

	@Override
	public int delete(int id) {
		return sqlSessionTemplate.delete("XxlDbDemoMapper.delete", id);
	}

	@Override
	public int update(XxlDbDemo xxlDbDemo) {
		XxlDbRouter.setDbType(DB_TYPE.WRITE);
		return sqlSessionTemplate.update("XxlDbDemoMapper.update", xxlDbDemo);
	}

	@Override
	public XxlDbDemo load(int id) {
		XxlDbRouter.setDbType(DB_TYPE.READ);
		return sqlSessionTemplate.selectOne("XxlDbDemoMapper.load", id);
	}

}
