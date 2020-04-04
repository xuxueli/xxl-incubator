package com.xxl.dao;

import com.xxl.core.model.XxlDbDemo;

public interface IDemoDao {

	public int save(XxlDbDemo xxlDbDemo);
	
	public int delete(int id);
	
	public int update(XxlDbDemo xxlDbDemo);
	
	public XxlDbDemo load(int id);

}
