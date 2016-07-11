package com.xxl.dao;

import java.util.List;

import com.xxl.core.model.main.DevTask;


/**
 * 开发任务
 * @author xuxueli
 */
public interface IDevTaskDao {

	public DevTask getByProcessInstanceId(String processInstanceId);

	public DevTask getById(int id);

	public int updateProcessInstanceId(int id, String processInstanceId);

	public int save(DevTask devTask);

	public List<DevTask> getByUserId(int userId);

	public int delete(int id);
	
}
