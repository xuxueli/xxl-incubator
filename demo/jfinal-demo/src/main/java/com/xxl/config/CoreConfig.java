package com.xxl.config;

import java.util.Map.Entry;
import java.util.Properties;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.FreeMarkerRender;
import com.jfinal.render.ViewType;
import com.xxl.controller.BlogController;
import com.xxl.controller.IndexController;
import com.xxl.model.Blog;

import freemarker.template.TemplateModelException;

/**
 * 核心Config配置文件
 * @author xuxueli
 */
public class CoreConfig extends JFinalConfig {

	/*
	 * 等价于listener【非必须】
	 * @see com.jfinal.config.JFinalConfig#afterJFinalStart()
	 */
	@Override
	public void afterJFinalStart() {
		try {
			// 设置freemarker属性
			Properties freemarker = loadPropertyFile("freemarker.properties");
			FreeMarkerRender.setProperties(freemarker);
			
			// 设置freemarker全局变量
			Properties variables = loadPropertyFile("freemarker.variables.properties");
			if (variables != null) {
				for (Entry<Object, Object> item : variables.entrySet()) {
					FreeMarkerRender.getConfiguration().setSharedVariable(String.valueOf(item.getKey()), String.valueOf(item.getValue()));
				}
			}
		} catch (TemplateModelException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 配置路由
	 * @see com.jfinal.config.JFinalConfig#configRoute(com.jfinal.config.Routes)
	 */
	@Override
	public void configRoute(Routes route) {
		route.add("/", IndexController.class, "/");	// 第三个参数为该Controller的视图存放路径
		route.add("/blog", BlogController.class, "/");
	}
	
	/*
	 * 配置常量
	 * @see com.jfinal.config.JFinalConfig#configConstant(com.jfinal.config.Constants)
	 */
	@Override
	public void configConstant(Constants constants) {
		// 设置freemarker模板
		constants.setViewType(ViewType.FREE_MARKER);	//默认的 可以不配置
		constants.setBaseViewPath("/WEB-INF/template");	//页面模板根路径
		constants.setFreeMarkerViewExtension("ftl");	//freemarker 模板后缀名
		
		// 设置开发模式
		loadPropertyFile("jdbc.properties");				// 加载系统属性配置文件,随后可用getProperty(...)获取值
		constants.setDevMode(Boolean.valueOf(getProperty("devMode")));
	}
	
	/*
	 * 配置插件
	 * @see com.jfinal.config.JFinalConfig#configPlugin(com.jfinal.config.Plugins)
	 */
	@Override
	public void configPlugin(Plugins plugin) {
		loadPropertyFile("jdbc.properties");
		/**
		 * 配置Mysql支持
		 */
		// 配置c3p0数据库连接池插件
		C3p0Plugin cp = new C3p0Plugin(getProperty("mysql.jdbcUrl"), getProperty("mysql.user"), getProperty("mysql.password"));
		plugin.add(cp);		
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		plugin.add(arp);
		
		// 映射table到model
		arp.addMapping("test_blog", Blog.class);
		
		/**
		 * 配置Oracle支持
		 */
		/*// 配置c3p0数据库连接池插件
		C3p0Plugin cp = new C3p0Plugin(getProperty("oracle.jdbcUrl"), getProperty("oracle.user"), getProperty("oracle.password")); 
		cp.setDriverClass(getProperty("jdbcDriver"));	// 配置Oracle驱动
		plugin.add(cp);
		// 配置ActiveRecord插件
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		plugin.add(arp);
		arp.setDialect(new OracleDialect());	// 配置Oracle方言
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());	// 配置属性名（字段名）大小写不敏感容器工厂
		
		// 映射table到model
		arp.addMapping("TEST_BLOG","ID", Blog.class);*/
	}
	
	/*
	 * 配置全局拦截器
	 * @see com.jfinal.config.JFinalConfig#configInterceptor(com.jfinal.config.Interceptors)
	 */
	@Override
	public void configInterceptor(Interceptors me) {
	}

	/*
	 * 配置处理器
	 * @see com.jfinal.config.JFinalConfig#configHandler(com.jfinal.config.Handlers)
	 */
	@Override
	public void configHandler(Handlers me) {
	}

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebContent", 80, "/", 5);
	}
}
