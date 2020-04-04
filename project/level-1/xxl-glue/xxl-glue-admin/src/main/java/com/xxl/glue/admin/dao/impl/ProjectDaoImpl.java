package com.xxl.glue.admin.dao.impl;

import com.xxl.glue.admin.core.model.Project;
import com.xxl.glue.admin.dao.IProjectDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ProjectDaoImpl implements IProjectDao {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int save(Project project) {
		return sqlSessionTemplate.insert("ProjectMapper.save", project);
	}

	@Override
	public int update(Project project) {
		return sqlSessionTemplate.update("ProjectMapper.update", project);
	}

	@Override
	public int delete(int id) {
		return sqlSessionTemplate.delete("ProjectMapper.delete", id);
	}

	@Override
	public List<Project> loadAll() {
		return sqlSessionTemplate.selectList("ProjectMapper.loadAll");
	}

	@Override
	public Project findByAppname(String appname) {
		return sqlSessionTemplate.selectOne("ProjectMapper.findByAppname", appname);
	}

	@Override
	public Project load(int id) {
		return sqlSessionTemplate.selectOne("ProjectMapper.load", id);
	}

}
