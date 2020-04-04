package com.xxl.glue.admin.dao.impl;

import com.xxl.glue.admin.core.model.CodeLog;
import com.xxl.glue.admin.dao.ICodeLogDao;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class CodeLogDaoImpl implements ICodeLogDao {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;
	
	@Override
	public int save(CodeLog codeLog) {
		return sqlSessionTemplate.insert("CodeLogMapper.save", codeLog);
	}
	
	@Override
	public List<CodeLog> findByGlueId(int glueId) {
		return sqlSessionTemplate.selectList("CodeLogMapper.findByGlueId", glueId);
	}

	@Override
	public int removeOldLogs(int glueId) {
		return sqlSessionTemplate.delete("CodeLogMapper.removeOldLogs", glueId);
	}

	@Override
	public int delete(int glueId) {
		return sqlSessionTemplate.delete("CodeLogMapper.delete", glueId);
	}

}
