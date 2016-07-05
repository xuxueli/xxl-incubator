package com.xxl.util.core.util;

import javax.servlet.ServletContext;

/**
 * ServletContext操作
 * 
 * <pre>
 * 作用范围：
 * 		Request ：保存的键值仅在下一个request对象中可以得到。
 * 		Session ：它是一个会话范围，相当于一个局部变量，从Session第一次创建知道关闭，数据都一直 保存，每一个客户都有一个Session，所以它可以被客户一直访问，只要Session没有关闭和超时即浏览器关闭。
 * 		ServletContext ：它代表了servlet环境的上下文，相当于一个全局变量，即只要某个web应用在启动中，这个对象就一直都有效的存在，所以它的范围是最大的，存储的数据可以被所有用户使用，只要服务器不关闭，数据就会一直都存在。
 * 优缺点：
 * 		Request ：
 * 			好处：用完就仍，不会导致资源占用的无限增长。
 * 			弊处：数据只能被下一个对象获取，所以在写程序时会因为无法共享数据导致每次要用都从数据库中取，多做操作，自然会对性能有一些影响。
 * 		Session ：
 * 			好处：是一个局部变量，可以保存用户的信息并直接取出，不用每次都去数据库抓，少做操作，极大的方便了程序的编写。
 * 			弊处：每个客户都有一个session，只能自己使用，不同session可能保存大量重复数据； 可能耗费大量服务器内存； 另外session构建在cookie和url重写的基础上，所以用session实现会话跟踪，会用掉一点点服务器带宽和客户端保持联络， 当然session越多，耗费的带宽越多，理论上也会对性能造成影响。 集群的session同步会是个问题。
 * 		ServletContext ：
 * 			好处：不用每次都去数据库抓，少做操作。 存储的数据所有客户都可以用。 可减少重复在内存中存储数据造成的开销。
 * </pre>
 * 
 * @author xuxueli 2015-5-5 18:25:54
 */
public class ServletContextUtil {

	/**
	 * 存入
	 * @param application
	 * @param key
	 * @param value
	 */
	public static void set(ServletContext application, String key, Object value){
		application.setAttribute(key, value);
	}
	
	/**
	 * 取出
	 * @param application
	 * @param key
	 * @return
	 */
	public static Object get(ServletContext application, String key){
		return application.getAttribute(key);
	}
	
	/**
	 * 移除
	 * @param application
	 * @param key
	 */
	public static void remove(ServletContext application, String key){
		application.removeAttribute(key);
	}
	
}
