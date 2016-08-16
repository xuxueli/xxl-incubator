package com.xxl.controller.validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;
import com.xxl.model.Blog;

public class BlogValidator extends Validator {

	/**
	 * 校验
	 */
	@Override
	protected void validate(Controller c) {
		validateRequiredString("blog.title", "titleMsg", "请输入Blog标题！");
		validateRequiredString("blog.content", "contentMsg", "请输入Blog内容！");
	}

	/**
	 * 异常处理
	 */
	@Override
	protected void handleError(Controller c) {
		c.keepModel(Blog.class);
		
		String actionKey = this.getActionKey();
		if("/blog/addDo".equals(actionKey)){
			c.render("blog/addPage.ftl");
		}else if("/blog/editDo".equals(actionKey)){
			c.render("blog/edit.ftl");
		}
		
	}

}
