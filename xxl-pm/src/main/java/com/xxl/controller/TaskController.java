package com.xxl.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.controller.core.BaseLoginController;
import com.xxl.core.constant.CommonDic.AvtivitiDic;
import com.xxl.core.constant.CommonDic.AvtivitiDic.DevTaskDefinitionKey;
import com.xxl.core.constant.CommonDic.ReturnCodeEnum;
import com.xxl.core.constant.CommonDic.ReturnTDic;
import com.xxl.core.exception.WebException;
import com.xxl.core.model.main.DevTask;
import com.xxl.core.result.ReturnT;
import com.xxl.service.IActivitiService;
import com.xxl.service.IDevTaskService;

/**
 * 用户相关
 * @author xuxueli
 */
@Controller
@RequestMapping("/task")
public class TaskController extends BaseLoginController {
	private static Logger logger = LoggerFactory.getLogger(TaskController.class);
	
	@Autowired
	private IActivitiService activitiService;
	@Autowired
	private IDevTaskService devTaskService;
	
	@RequestMapping("")
	@PermessionType
	public String index(HttpServletRequest request, HttpSession session, Model model){
		// 开发任务列表
		List<DevTask> devTaskList = devTaskService.getByUserId(super.getLoginIdentity().getUserId());
		model.addAttribute("devTaskList", devTaskList);
		
		// 流程任务
		List<Task> taskAll = activitiService.queryTask(String.valueOf(super.getLoginIdentity().getUserId()));
		if (CollectionUtils.isNotEmpty(taskAll)) {
			for (DevTaskDefinitionKey item : AvtivitiDic.DevTaskDefinitionKey.values()) {
				List<Task> temp = new ArrayList<Task>();
				for (Task task : taskAll) {
					if (item.name().equals(task.getTaskDefinitionKey())) {
						temp.add(task);
					}
				}
				model.addAttribute(item.name(), temp);
			}
			// 任务1-批注列表
			List<Comment> CommentList = activitiService.getProcessInstanceComments(taskAll.get(0).getProcessInstanceId());
			if (CollectionUtils.isNotEmpty(CommentList)) {
				for (Comment comment : CommentList) {
					logger.info("time:{}, userId:{}, msg:{}, toString:{}", 
							new Object[]{comment.getTime(), comment.getUserId(), comment.getFullMessage(), comment.toString()});
				}
			}
		}
		
		
		return "front/task/index";
	}
	
	@RequestMapping("/createDevTask")
	@PermessionType
	@ResponseBody
	public ReturnT<String> createDevTask(DevTask devTask){
		devTask.setUserId(super.getLoginIdentity().getUserId());
		devTaskService.save(devTask);
		return ReturnTDic.success;
	}
	
	@RequestMapping("/deleteDevTask/{id}")
	@PermessionType
	@ResponseBody
	public ReturnT<String> deleteDevTask(@PathVariable("id") int id){
		DevTask devTask = devTaskService.getById(id);
		if (devTask == null) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "任务不存在或已被删除");
		}
		if (StringUtils.isNotBlank(devTask.getProcessInstanceId())) {
			String processInstanceId = devTask.getProcessInstanceId();
			activitiService.deleteProcessInstance(processInstanceId, "正常删除");
		}
		
		devTaskService.delete(id);
		return ReturnTDic.success;
	}
	
	
	@RequestMapping("/startProcess/{id}")
	@PermessionType
	@ResponseBody
	public ReturnT<String> startProcess(@PathVariable("id") int id){
		DevTask devTask = devTaskService.getById(id);
		if (devTask == null) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "任务不存在或已被删除");
		}
		if (StringUtils.isNotBlank(devTask.getProcessInstanceId())) {
			return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "任务流程已启动");
		}
		
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put(AvtivitiDic.assignee_to, String.valueOf(super.getLoginIdentity().getUserId()));
		String processInstanceId = activitiService.startProcessInstanceByKey(AvtivitiDic.dev_task, String.valueOf(devTask.getId()), variables);
		
		devTaskService.updateProcessInstanceId(devTask.getId(), processInstanceId);
		return ReturnTDic.success;
	}
	
	@RequestMapping("/completeTask/{taskId}/{status}")
	@PermessionType
	@ResponseBody
	public ReturnT<String> completeTask(@PathVariable("taskId") String taskId, @PathVariable("status") String status){
		String commontMsg = "";
		Map<String, Object> variables = new HashMap<String,Object>();
		if (StringUtils.isNotBlank(status)) {
			if (status.equals(String.valueOf(true))) {
				commontMsg = super.getLoginIdentity().getRealName() + "：审核通过";
			} else if (status.equals(String.valueOf(false))) {
				commontMsg = super.getLoginIdentity().getRealName() + "：审核拒绝";
			} else {
				return new ReturnT<String>(ReturnCodeEnum.FAIL.code(), "审核状态参数非法");
			}
			variables.put(AvtivitiDic.status, status);
		}
		activitiService.completeTask(commontMsg, taskId, variables);
		return ReturnTDic.success;
	}
	
	@RequestMapping("/showProcess")
	@PermessionType
	public String showProcess(String processInstanceId, Model model){
		ProcessInstance pi = activitiService.getProcessInstance(processInstanceId);
		if (pi != null) {
			model.addAttribute("processDefinitionId", pi.getProcessDefinitionId());
			model.addAttribute("processInstanceId", pi.getProcessInstanceId());
			return "front/task/showProcess";
		} else {
			throw new WebException("流程不存在"); 
		}
	}

}
