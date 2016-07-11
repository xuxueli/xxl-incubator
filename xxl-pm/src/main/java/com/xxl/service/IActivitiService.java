package com.xxl.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;

/**
 * activiti
 * @author xuxueli
 */
public interface IActivitiService {
	
	/**
	 * 启动.流程实例
	 * @param processDefinitionKey	: 流程定义key
	 * @param businessKey			: 业务key
	 * @param variables				: 流程变量
	 * @return		: 流程实例id
	 */
	public String startProcessInstanceByKey(String processDefinitionKey, String businessKey, Map<String, Object> variables);
	
	/**
	 * 删除.流程实例
	 * @param processInstanceId
	 * @param deleteReason
	 */
	public void deleteProcessInstance(String processInstanceId, String deleteReason);

	/**
	 * 查询.任务
	 * @param assignee
	 */
	public List<Task> queryTask(String assignee);
	
	/**
	 * 完成.任务
	 * @param taskId
	 * @param variables
	 */
	public void completeTask(String commentMsg, String taskId, Map<String, Object> variables);

	/**
	 * 查询-流程实例
	 * @param processInstanceId
	 * @return
	 */
	public ProcessInstance getProcessInstance(String processInstanceId);

	/**
	 * 批注信息
	 * @param processInstanceId
	 * @return
	 */
	public List<Comment> getProcessInstanceComments(String processInstanceId);
}
