package com.xxl.util.core.skill.xml.xml;

import com.xxl.util.core.skill.xml.model.User;
import com.xxl.util.core.skill.xml.xml.impl.XmlDom4jImpl;

import java.util.ArrayList;
import java.util.List;

public class XmlDemo {
	private static String fileName = "UserList.xml";
	private static boolean ifCreate = false;	// true	false
	public static void main(String[] args) throws Exception {
		//IXml util = new XmlDomImpl();	// 与dom4j冲突,取消dom4j的maven依赖才可运行
		//IXml util = new XmlSaxImpl();
		IXml util = new XmlDom4jImpl();
		
		if (ifCreate) {
			List<User> list = new ArrayList<User>();
			for (int i = 0; i < 8; i++) {
				list.add(new User(i, "姓名"+i, 18+i, i%2==0?"女":"男"));
			}
			util.createXml(list, fileName);
			System.out.println("createXml ok");
		} else {
			List<User> list = util.parserXml(fileName);
			if (list != null) {
				for (User user : list) {
					System.out.println(user.toString());
				}
				System.out.println("xml parse size:" + list.size());
			} else {
				System.out.println("xml parse null");
			}
		}
	}
	
}
