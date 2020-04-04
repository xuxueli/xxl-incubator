package com.xxl.service;

import com.xxl.core.model.XxlDbDemo;

public interface IDemoService {

	public int save(XxlDbDemo xxlDbDemo);

	public int delete(int id);

	public int update(XxlDbDemo xxlDbDemo);

	public XxlDbDemo load(int id);

}
