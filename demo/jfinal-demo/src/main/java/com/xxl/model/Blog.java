package com.xxl.model;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

/**
 * Blog-Model类
 * @author xuxueli
 * 
 * id		int(11)	NO	PRI		auto_increment
 * title	varchar(200)	YES	
 * content	text	YES			
 */

@SuppressWarnings("serial")
public class Blog extends Model<Blog> {

	
	public static final Blog dao = new Blog();	// 方便于访问数据库，不是必须

	/**
	 * 分页查询 【所有 sql 与业务逻辑写在 Model 或 Service 中 方法说明】
	 * @param pageNumber
	 * @param pageSize
	 * @return
	 */
	public Page<Blog> paginate(int pageNumber, int pageSize) {
		// jfinal的强大之处,mysql和oracle的统一,且一行搞定
		return paginate(pageNumber, pageSize, "select * ","from test_blog order by id asc");	
	}
}
