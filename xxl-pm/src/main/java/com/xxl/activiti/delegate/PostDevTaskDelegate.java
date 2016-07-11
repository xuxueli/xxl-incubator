package com.xxl.activiti.delegate;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 开发任务-流程后处理
 * @author xuxueli
 */
@Component("postDevTaskDelegate")
public class PostDevTaskDelegate implements JavaDelegate{
	private static Logger logger = LoggerFactory.getLogger(PostDevTaskDelegate.class);
	
	//private Expression statusParam;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		//String assignee = (String) execution.getVariable(AvtivitiDic.assignee);	// 流程变量
		//String status = (String) statusParam.getValue(execution);					// 属性注入 (改属性必须为静态固定值,因为该类为单例,该属性仅第一此实例化时注入)
		
		String processInstanceId = execution.getProcessInstanceId();
		logger.info("postDevTaskDelegate, processInstanceId:{}", new Object[]{processInstanceId});
	}
	
}
