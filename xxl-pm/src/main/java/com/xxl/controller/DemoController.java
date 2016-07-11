package com.xxl.controller;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xxl.controller.annotation.PermessionType;
import com.xxl.core.result.ReturnT;

/**
 * 用户操作
 * @author xuxueli
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
	
	
	@RequestMapping(value="/{secretKey}/{funCode}")
	@ResponseBody
	@PermessionType(loginState=false)
	public ReturnT<String> test(@PathVariable("secretKey")String secretKey, @PathVariable("funCode")int funCode){
		if (!StringUtils.equals("xuxueli", secretKey)) {
			return null;
		}

		switch (funCode) {
		case 5000:
			break;
		default:
			break;
		}
		
		return new ReturnT<String>();
	}
	
	//@Autowired
	//RepositoryService repositoryService;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	HistoryService historyService;
	
	@RequestMapping(value="/test2")
	@PermessionType(loginState=false)
	@ResponseBody
	public String test2(){

		String processId = runtimeService.startProcessInstanceByKey("Interview").getId();
		
		// 得到笔试的流程
		System.out.println("***************笔试流程开始***************");
		List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();
		for (Task task : tasks) {
			System.out.println("人力资源部的任务：name:" + task.getName() + ",id:" 	+ task.getId());
			taskService.claim(task.getId(), "张三");
		}
		System.out.println("张三的任务数量："	+ taskService.createTaskQuery().taskAssignee("张三").count());
		
		tasks = taskService.createTaskQuery().taskAssignee("张三").list();
		for (Task task : tasks) {
			System.out.println("张三的任务：name:" + task.getName() + ",id:" + task.getId());
			taskService.complete(task.getId());
		}
		System.out.println("张三的任务数量："	+ taskService.createTaskQuery().taskAssignee("张三").count());
		System.out.println("***************笔试流程结束***************");

		System.out.println("***************一面流程开始***************");
		tasks = taskService.createTaskQuery().taskCandidateGroup("技术部").list();
		for (Task task : tasks) {
			System.out.println("技术部的任务：name:" + task.getName() + ",id:" + task.getId());
			taskService.claim(task.getId(), "李四");
		}
		System.out.println("李四的任务数量：" + taskService.createTaskQuery().taskAssignee("李四").count());
		
		for (Task task : tasks) {
			System.out.println("李四的任务：name:" + task.getName() + ",id:"	+ task.getId());
			taskService.complete(task.getId());
		}
		System.out.println("李四的任务数量："	+ taskService.createTaskQuery().taskAssignee("李四").count());
		System.out.println("***************一面流程结束***************");

		System.out.println("***************二面流程开始***************");
		tasks = taskService.createTaskQuery().taskCandidateGroup("技术部").list();
		for (Task task : tasks) {
			System.out.println("技术部的任务：name:" + task.getName() + ",id:" + task.getId());
			taskService.claim(task.getId(), "李四");
		}
		System.out.println("李四的任务数量：" + taskService.createTaskQuery().taskAssignee("李四").count());
		
		for (Task task : tasks) {
			System.out.println("李四的任务：name:" + task.getName() + ",id:" + task.getId());
			taskService.complete(task.getId());
		}
		System.out.println("李四的任务数量：" + taskService.createTaskQuery().taskAssignee("李四").count());
		System.out.println("***************二面流程结束***************");

		System.out.println("***************HR面流程开始***************");
		tasks = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();
		for (Task task : tasks) {
			System.out.println("技术部的任务：name:" + task.getName() + ",id:" + task.getId());
			taskService.claim(task.getId(), "李四");
		}
		System.out.println("李四的任务数量：" + taskService.createTaskQuery().taskAssignee("李四").count());
		
		for (Task task : tasks) {
			System.out.println("李四的任务：name:" + task.getName() + ",id:" + task.getId());
			taskService.complete(task.getId());
		}
		System.out.println("李四的任务数量：" + taskService.createTaskQuery().taskAssignee("李四").count());
		System.out.println("***************HR面流程结束***************");

		System.out.println("***************录用流程开始***************");
		tasks = taskService.createTaskQuery().taskCandidateGroup("人力资源部").list();
		for (Task task : tasks) {
			System.out.println("技术部的任务：name:" + task.getName() + ",id:" + task.getId());
			taskService.claim(task.getId(), "李四");
		}
		System.out.println("李四的任务数量：" + taskService.createTaskQuery().taskAssignee("李四").count());
		for (Task task : tasks) {
			System.out.println("李四的任务：name:" + task.getName() + ",id:" + task.getId());
			taskService.complete(task.getId());
		}
		System.out.println("李四的任务数量：" + taskService.createTaskQuery().taskAssignee("李四").count());
		System.out.println("***************录用流程结束***************");

		HistoricProcessInstance historicProcessInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processId).singleResult();
		System.out.println("流程结束时间：" + historicProcessInstance.getEndTime());
	
		return "S";
	}
	
	
}
