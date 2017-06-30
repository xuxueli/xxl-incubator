package com.xxl.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xxl.core.model.main.DevTask;
import com.xxl.dao.IDevTaskDao;

/**
 * 开发任务
 * @author xuxueli
 */
@Repository
public class DevTaskDaoImpl implements IDevTaskDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public DevTask getByProcessInstanceId(String processInstanceId) {
		return sqlSessionTemplate.selectOne("DevTaskMapper.getByProcessInstanceId", processInstanceId);
	}

	@Override
	public DevTask getById(int id) {
		return sqlSessionTemplate.selectOne("DevTaskMapper.getById", id);
	}

	@Override
	public int updateProcessInstanceId(int id, String processInstanceId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("processInstanceId", processInstanceId);
		return sqlSessionTemplate.update("DevTaskMapper.updateProcessInstanceId", params);
	}

	@Override
	public int save(DevTask devTask) {
		return sqlSessionTemplate.insert("DevTaskMapper.save", devTask);
	}

	@Override
	public List<DevTask> getByUserId(int userId) {
		return sqlSessionTemplate.selectList("DevTaskMapper.getByUserId", userId);
	}

	@Override
	public int delete(int id) {
		return sqlSessionTemplate.delete("DevTaskMapper.delete", id);
	}

}
