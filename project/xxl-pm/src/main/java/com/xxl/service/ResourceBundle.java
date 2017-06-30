package com.xxl.service;

import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

/**
 * 资源实例
 * @author xuxueli
 */
public class ResourceBundle {

	private static final ResourceBundle resource = new ResourceBundle();

	public static ResourceBundle getInstance() {
		return resource;
	}

	private FreeMarkerConfig freemarkerConfig;

	public FreeMarkerConfig getFreemarkerConfig() {
		return freemarkerConfig;
	}
	public void setFreemarkerConfig(FreeMarkerConfig freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}
	
}
