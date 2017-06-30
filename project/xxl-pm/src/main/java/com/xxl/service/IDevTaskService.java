package com.xxl.service;

import java.util.List;

import com.xxl.core.model.main.DevTask;
import com.xxl.core.result.ReturnT;


/**
 * 开发任务
 * @author xuxueli
 */
public interface IDevTaskService {

	public DevTask getByProcessInstanceId(String processInstanceId);

	public DevTask getById(int id);

	public int updateProcessInstanceId(int id, String processInstanceId);

	public ReturnT<String> save(DevTask devTask);

	public List<DevTask> getByUserId(int userId);

	public int delete(int id);
	
}
