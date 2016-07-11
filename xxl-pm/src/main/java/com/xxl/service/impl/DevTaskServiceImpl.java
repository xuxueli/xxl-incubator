package com.xxl.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.constant.CommonDic.ReturnTDic;
import com.xxl.core.model.main.DevTask;
import com.xxl.core.result.ReturnT;
import com.xxl.dao.IDevTaskDao;
import com.xxl.service.IDevTaskService;

/**
 * 开发任务
 * @author xuxueli
 */
@Service()
public class DevTaskServiceImpl implements IDevTaskService {
	
	@Autowired
	private IDevTaskDao devTaskDao;

	@Override
	public DevTask getByProcessInstanceId(String processInstanceId) {
		return devTaskDao.getByProcessInstanceId(processInstanceId);
	}

	@Override
	public DevTask getById(int id) {
		return devTaskDao.getById(id);
	}

	@Override
	public int updateProcessInstanceId(int id, String processInstanceId) {
		return devTaskDao.updateProcessInstanceId(id, processInstanceId);
	}

	@Override
	public ReturnT<String> save(DevTask devTask) {
		if (StringUtils.isBlank(devTask.getTitle())) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "任务标题不可为空") ;
		}
		devTaskDao.save(devTask);
		return ReturnTDic.success;
	}

	@Override
	public List<DevTask> getByUserId(int userId) {
		return devTaskDao.getByUserId(userId);
	}

	@Override
	public int delete(int id) {
		return devTaskDao.delete(id);
	}
	
}
