package com.xxl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.model.XxlDbDemo;
import com.xxl.dao.IDemoDao;
import com.xxl.service.IDemoService;

@Service
public class DemoServiceImpl implements IDemoService {

	@Autowired
	private IDemoDao demoDao;
	
	@Override
	public int save(XxlDbDemo xxlDbDemo) {
		return demoDao.save(xxlDbDemo);
	}

	@Override
	public int delete(int id) {
		return demoDao.delete(id);
	}

	@Override
	public int update(XxlDbDemo xxlDbDemo) {
		return demoDao.update(xxlDbDemo);
	}

	@Override
	public XxlDbDemo load(int id) {
		return demoDao.load(id);
	}
	
}
