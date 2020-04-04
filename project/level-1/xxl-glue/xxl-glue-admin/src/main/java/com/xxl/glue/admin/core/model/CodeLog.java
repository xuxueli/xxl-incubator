package com.xxl.glue.admin.core.model;

import java.util.Date;

/**
 * @author xuxueli 2016-1-1 19:18:58
 */
public class CodeLog {
	
	private int id;
	private int glueId;
	private String remark;
	private String source;
	private Date addTime;
	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getGlueId() {
		return glueId;
	}

	public void setGlueId(int glueId) {
		this.glueId = glueId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
