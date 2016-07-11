package com.xxl.activiti.listener;

import java.util.List;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xxl.core.constant.CommonDic.AvtivitiDic;
import com.xxl.core.constant.CommonDic.AvtivitiDic.DevTaskDefinitionKey;
import com.xxl.core.model.main.UserMain;
import com.xxl.dao.IUserMainDao;

/**
 * 指定“任务.处理人”
 * @author xuxueli
 */
@SuppressWarnings("serial")
@Component("assignTaskListener")
public class AssignTaskListener implements TaskListener {
	private static Logger logger = LoggerFactory.getLogger(AssignTaskListener.class);
	
	@Autowired
	private TaskService taskService;
	@Autowired
	private IUserMainDao userMainDao;
	
	// private Expression assignee_type;	// 属性注入 (改属性必须为静态固定值,因为该类为单例,该属性仅第一此实例化时注入)
	
	@Override
	public void notify(DelegateTask delegateTask) {
		// String assignee_object = (String) delegateTask.getExecution().getVariable(AvtivitiDic.assignee_object);
		
		UserMain userMain = null;
		if (DevTaskDefinitionKey.dev.name().equals(delegateTask.getTaskDefinitionKey()) && false) {
			// 任务节点-指派：dev
			String assignee_to = (String) delegateTask.getExecution().getVariable(AvtivitiDic.assignee_to); 
			userMain = userMainDao.getByUserId(Integer.valueOf(assignee_to));
		} else if (DevTaskDefinitionKey.test.name().equals(delegateTask.getTaskDefinitionKey())) {
			// 任务节点-指派：test
			List<UserMain> list = userMainDao.getByRole(DevTaskDefinitionKey.test.name());
			if (CollectionUtils.isNotEmpty(list)) {
				userMain = list.get(RandomUtils.nextInt(0, list.size()-1));
			}
		} else if (DevTaskDefinitionKey.audit.name().equals(delegateTask.getTaskDefinitionKey())) {
			// 任务节点-指派：audit
			List<UserMain> list = userMainDao.getByRole(DevTaskDefinitionKey.audit.name());
			if (CollectionUtils.isNotEmpty(list)) {
				userMain = list.get(RandomUtils.nextInt(0, list.size()-1));
			}
		}
		
		if (userMain != null) {
			delegateTask.setAssignee(String.valueOf(userMain.getUserId()));
			logger.info("assignTaskListener success, piId:{}, taskId:{}, taskName:{}, assignee to:{}", 
					new Object[]{delegateTask.getProcessInstanceId(), delegateTask.getId(), delegateTask.getName(), userMain.getUserId()});
		} else {
			logger.info("assignTaskListener fail, piId:{}, taskId:{}, taskName:{}", 
					new Object[]{delegateTask.getProcessInstanceId(), delegateTask.getId(), delegateTask.getName()});
		}
		
	}

}
