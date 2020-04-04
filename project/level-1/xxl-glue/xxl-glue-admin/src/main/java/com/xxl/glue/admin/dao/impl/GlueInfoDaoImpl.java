package com.xxl.glue.admin.dao.impl;

import com.xxl.glue.admin.core.model.GlueInfo;
import com.xxl.glue.admin.dao.IGlueInfoDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GlueInfoDaoImpl implements IGlueInfoDao {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public List<GlueInfo> pageList(int offset, int pagesize, int projectId, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("projectId", projectId);
		params.put("name", name);
		
		return sqlSessionTemplate.selectList("GlueInfoMapper.pageList", params);
	}

	@Override
	public int pageListCount(int offset, int pagesize, int projectId, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("offset", offset);
		params.put("pagesize", pagesize);
		params.put("projectId", projectId);
		params.put("name", name);
		
		return sqlSessionTemplate.selectOne("GlueInfoMapper.pageListCount", params);
	}

	@Override
	public int delete(int id) {
		return sqlSessionTemplate.delete("GlueInfoMapper.delete", id);
	}

	@Override
	public int save(GlueInfo codeInfo) {
		return sqlSessionTemplate.insert("GlueInfoMapper.save", codeInfo);
	}

	@Override
	public int update(GlueInfo codeInfo) {
		return sqlSessionTemplate.update("GlueInfoMapper.update", codeInfo);
	}

	@Override
	public GlueInfo load(int id) {
		return sqlSessionTemplate.selectOne("GlueInfoMapper.load", id);
	}

	@Override
	public GlueInfo loadCodeByName(String name) {
		return sqlSessionTemplate.selectOne("GlueInfoMapper.loadByName", name);
	}

}
