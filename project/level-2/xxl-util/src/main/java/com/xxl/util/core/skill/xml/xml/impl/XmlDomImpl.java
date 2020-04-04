package com.xxl.util.core.skill.xml.xml.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.xxl.util.core.skill.xml.model.User;
import com.xxl.util.core.skill.xml.xml.IXml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * DOM：在现在的Java JDK里都自带了，在xml-apis.jar包里
 * 
 * 【默认正常：org.w3c.dom.Element：Copyright (c) 2004 World Wide Web Consortium】
 * 【maven+dom4j报错：org.w3c.dom.Element：Copyright (c) 1998 by W3C】
 */
public class XmlDomImpl implements IXml {

	@Override
	public void createXml(List<User> list, String fileName) throws Exception {
		// step1:获得一个DocumentBuilderFactory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// step2:获得一个DocumentBuilder
		DocumentBuilder db = factory.newDocumentBuilder();
		// step3:新建一个Document对象
		Document document = db.newDocument();
		// step4:创建一个根节点
		Element rootElement = document.createElement("users");
		for (User item : list) {
			// step5:创建一个节点
			Element user = document.createElement("user");
			
			// step6:为该节点设定属性
			Element name = document.createElement("name");
			name.setTextContent(item.getName());
			Element age = document.createElement("age");
			age.setTextContent(String.valueOf(item.getAge()));
			Element sex = document.createElement("sex");
			sex.setTextContent(item.getSex());

			user.setAttribute("id", String.valueOf(item.getId()));
			user.appendChild(name);
			user.appendChild(age);
			user.appendChild(sex);
			// step7:为某一元素节点设立子节点
			rootElement.appendChild(user);
		}
		// step8:把刚刚建立的根节点添加到document对象中
		document.appendChild(rootElement);
		// step9:获得一个TransformerFactory对象
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		// step10:获得一个Transformer对象
		Transformer transformer = transformerFactory.newTransformer();
		// step11:把document对象用一个DOMSource对象包装起来
		Source xmlSource = new DOMSource(document);
		// step12:建立一个存储目标对象
		Result outputTarget = new StreamResult(new File(fileName));	// Thread.currentThread().getContextClassLoader().getResource("").getPath() 
		// step13:生成相应的xml文件
		transformer.transform(xmlSource, outputTarget);
	}

	@Override
	public List<User> parserXml(String fileName) throws Exception {
		List<User> list = new ArrayList<User>();
		
		// step1:获得DocumentBuilderFactory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// step2:获得DocumentBuilder
		DocumentBuilder db = factory.newDocumentBuilder();
		// step3:把需要解析的xml文件加载到一个document对象中
		Document document = db.parse(fileName);
		// 获取所有名称为users的元素
		NodeList userList = document.getElementsByTagName("user");
		for (int i = 0; i < userList.getLength(); i++) {
			// 因为这里我知道它就是一个Element对象，所以进行了强转
			Element user = (Element) userList.item(i);
			String id = user.getAttribute("id");// 获得Element对象的属性
			
			String name = user.getElementsByTagName("name").item(0).getTextContent();
			String age = user.getElementsByTagName("age").item(0).getTextContent();
			String sex = user.getElementsByTagName("sex").item(0).getTextContent();
			
			User userItem = new User(Integer.valueOf(id), name, Integer.valueOf(age), sex);
			list.add(userItem);
			
		}

		return list;
	}
}
