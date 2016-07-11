package com.xxl.service.impl;

import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xxl.core.constant.CommonDic.AvtivitiDic;
import com.xxl.service.IActivitiService;

/**
 * activiti
 * @author xuxueli
 */
@Service
public class ActivitiServiceImpl implements IActivitiService {
	private static Logger logger = LoggerFactory.getLogger(ActivitiServiceImpl.class);
	
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	HistoryService historyService;
	
	@Override
	public String startProcessInstanceByKey(String processDefinitionKey,
			String businessKey, Map<String, Object> variables) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(AvtivitiDic.dev_task, businessKey, variables);
		
		logger.info("startProcessInstanceByKey successed, pdKey:{}, buKey:{}, var:{}, pdId:{}", 
				new Object[]{processDefinitionKey, businessKey, variables, pi.getId()});
		return pi.getId();
	}
	
	@Override
	public void deleteProcessInstance(String processInstanceId, String deleteReason) {
		if (getProcessInstance(processInstanceId) != null) {
			runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
		}
	}

	@Override
	public List<Task> queryTask(String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee).list();
	}

	@Override
	public void completeTask(String commentMsg, String taskId, Map<String, Object> variables) {
		// 添加批注
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.addComment(taskId, task.getProcessInstanceId(), commentMsg);
		
		// 办理任务
		taskService.complete(taskId, variables);
	}

	@Override
	public ProcessInstance getProcessInstance(String processInstanceId) {
		return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	}

	@Override
	public List<Comment> getProcessInstanceComments(String processInstanceId) {
		return taskService.getProcessInstanceComments(processInstanceId);
	}

}
