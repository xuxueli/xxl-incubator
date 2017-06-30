package com.xxl.util.core.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 资源路径Util
 * @author xuxueli
 */
public class WebPathUtil {
	
	/**
	 * ClassPath (通常properties 目录)
	 * @return
	 */
	public static String classPath(){
		return Thread.currentThread().getContextClassLoader().getResource("").getPath();
	}
	
	/**
	 * Web跟路径 (通常为样式文件/静态页面 目录) (不依赖于request)
	 * @return
	 */
	public static String webPath(){
		String realPath = classPath();
		int wei = realPath.lastIndexOf("WEB-INF/classes/");
		if (wei > -1) {
			realPath = realPath.substring(0, wei);
		}
		return realPath;
	}
	
	/**
	 * Web跟路径 (依赖于request)
	 * @param request
	 * @return
	 */
	public static String webPath(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("/");
	}
	
	/**
	 * WEB-INF目录 (通常为template文件 目录)
	 * @return
	 */
	public static String webInfPath(){
		String realPath = classPath();
		int wei = realPath.lastIndexOf("WEB-INF/classes/");
		if (wei > -1) {
			realPath = realPath.substring(0, wei);
		}
		return realPath + "WEB-INF/";
	}
	
	public static void main(String[] args) {
		System.out.println(webInfPath());
	}
	
}
