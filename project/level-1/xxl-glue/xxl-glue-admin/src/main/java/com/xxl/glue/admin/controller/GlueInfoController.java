package com.xxl.glue.admin.controller;

import com.xxl.glue.admin.core.model.GlueInfo;
import com.xxl.glue.admin.core.model.CodeLog;
import com.xxl.glue.admin.core.model.Project;
import com.xxl.glue.admin.core.result.ReturnT;
import com.xxl.glue.admin.dao.IProjectDao;
import com.xxl.glue.admin.service.IGlueInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/glueinfo")
public class GlueInfoController {
	
	@Resource
	private IGlueInfoService glueInfoService;
	@Resource
	private IProjectDao projectDao;

	@RequestMapping
	public String index(Model model){

		List<Project> projectList = projectDao.loadAll();
		model.addAttribute("projectList", projectList);

		return "glueinfo/glueinfo.list";
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
			@RequestParam(required = false, defaultValue = "10") int length, int projectId, String name){
		return glueInfoService.pageList(start, length, projectId, name);
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public ReturnT<String> delete(int id){
		return glueInfoService.delete(id);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public ReturnT<String> add(GlueInfo codeInfo){
		return glueInfoService.add(codeInfo);
	}

	@RequestMapping("/update")
	@ResponseBody
	public ReturnT<String> update(GlueInfo codeInfo){
		return glueInfoService.update(codeInfo);
	}


	@RequestMapping("/clearCache")
	@ResponseBody
	public ReturnT<String> clearCache(int id, String appNames){
		return glueInfoService.clearCache(id, appNames);
	}
	
	@RequestMapping("/glueWebIde")
	public String codeSourceEditor(Model model, int id){
		GlueInfo codeInfo = glueInfoService.load(id);
		model.addAttribute("codeInfo", codeInfo);
		
		if (codeInfo!=null) {
			List<CodeLog> codeLogList = glueInfoService.loadLogs(id);
			model.addAttribute("codeLogList", codeLogList);
		}
		
		return "glueinfo/glue.webide";
	}
	
	@RequestMapping("/updateCodeSource")
	@ResponseBody
	public ReturnT<String> updateCodeSource(HttpServletRequest request, CodeLog codeLog){
		return glueInfoService.updateCodeSource(codeLog);
	}

}
