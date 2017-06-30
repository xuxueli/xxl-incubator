package com.xxl.core.model.main;

import java.io.Serializable;

/**
 * 开发任务
 * @author xuxueli
 */
@SuppressWarnings("serial")
public class DevTask implements Serializable {
	
	private int id;
	private int userId;
	private String title;
	private String processInstanceId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
}
