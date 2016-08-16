package com.xxl.controller;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Page;
import com.xxl.controller.interceptor.BlogInterceptor;
import com.xxl.controller.validator.BlogValidator;
import com.xxl.model.Blog;

@Before(BlogInterceptor.class)
public class BlogController extends Controller {
	
	/**
	 * 博客列表首页	【index默认首页】
	 */
	public void index(){
		Page<Blog> pager = Blog.dao.paginate(getParaToInt("pageNumber", 1), 10);
		setAttr("pager", pager);
		render("blog/blogMain.ftl");
	}
	
	/**
	 * 新增page
	 */
	public void addPage(){
		render("blog/addPage.ftl");
	}
	
	/**
	 * 新增do
	 */
	@Before(BlogValidator.class)
	public void addDo(){
		Blog blog = this.getModel(Blog.class);
		//blog.set("id", "test_seq_blog.nextval");	//Oracle数据库使用序列
		blog.save();
		redirect("/blog");
	}
	
	/**
	 * 修改page
	 */
	public void edit(){
		Blog blog = Blog.dao.findById(getParaToInt());
		blog.put("page_title", "修改");
		
		setAttr("blog", blog);
		render("blog/edit.ftl");
	}
	
	/**
	 * 修改do
	 */
	@Before(BlogValidator.class)
	public void editDo(){
		Blog blog = this.getModel(Blog.class);
		blog.update();
		redirect("/blog");
	}
	
	/**
	 * 删除
	 */
	public void delete(){
		Blog.dao.deleteById(getParaToInt());
		redirect("/blog");
	}
	
	/**
	 * 查看
	 */
	public void view(){
		this.setAttr("blog", Blog.dao.findById(getParaToInt()).put("page_title", "查看"));
		render("blog/view.ftl");
	}
	
}
