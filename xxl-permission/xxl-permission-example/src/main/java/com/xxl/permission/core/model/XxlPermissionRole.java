package com.xxl.permission.core.model;

/**
 * 角色表
 * @author xuxueli
 */
public class XxlPermissionRole {
	
	private int id;
	private String name;
	private int order;
	
	// 角色是否拥有
	private boolean selected;
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	
}
