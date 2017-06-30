package com.xxl.controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller {

	public void index(){
		renderFreeMarker("index.ftl");
	}
}
