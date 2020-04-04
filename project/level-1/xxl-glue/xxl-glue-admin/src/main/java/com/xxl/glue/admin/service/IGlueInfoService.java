package com.xxl.glue.admin.service;

import com.xxl.glue.admin.core.model.GlueInfo;
import com.xxl.glue.admin.core.model.CodeLog;
import com.xxl.glue.admin.core.result.ReturnT;

import java.util.List;
import java.util.Map;

public interface IGlueInfoService {

	public Map<String, Object> pageList(int offset, int pagesize, int projectId, String name);
	
	public ReturnT<String> delete(int id);
	
	public ReturnT<String> add(GlueInfo codeInfo);

	ReturnT<String> update(GlueInfo codeInfo);

	public GlueInfo load(int id);

	public ReturnT<String> clearCache(int id, String appNames);
	
	public ReturnT<String> updateCodeSource(CodeLog codeLog);

	public List<CodeLog> loadLogs(int glueId);

}
