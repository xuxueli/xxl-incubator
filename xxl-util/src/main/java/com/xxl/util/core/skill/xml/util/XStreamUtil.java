package com.xxl.util.core.skill.xml.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.xxl.util.core.skill.xml.model.User;

import java.util.ArrayList;
import java.util.List;

public class XStreamUtil {

	/**
	 * Bean 2 XML
	 * 
	 * @param obj
	 * @return
	 */
	public static String bean2xml(Object obj) {
		XStream xs = new XStream();
		// xs.alias("root", BaseBean.class); // 类，指定别名
		// xs.aliasField("list", BaseBean.class, "userList"); // 类属性，指定别名
		return xs.toXML(obj);
	}

	/**
	 * XML 2 BEAN
	 * 
	 * @param xmlStr
	 * @return
	 */
	public static Object xml2bean(String xmlStr) {
		XStream xs = new XStream(new DomDriver());
		return xs.fromXML(xmlStr);
	}
	
	public static void main(String[] args) {
		// javabean 转 xml
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 9; i++) {
			users.add(new User(i, "姓名"+i, 18+i, i%2==0?"女":"男"));
		}
		String xml = bean2xml(users);
		System.out.println("javabean转成xml为:\n" + xml);

		// xml转javabean
		List<User> list = new ArrayList<User>();
		list = (List<User>) xml2bean(xml);
		System.out.println("xml转成javabean为:");
		for (User user : list) {
			System.out.println(user.toString());
		}
	}

}