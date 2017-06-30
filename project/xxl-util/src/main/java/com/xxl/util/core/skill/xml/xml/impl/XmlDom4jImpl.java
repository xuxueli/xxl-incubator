package com.xxl.util.core.skill.xml.xml.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.xxl.util.core.skill.xml.model.User;
import com.xxl.util.core.skill.xml.xml.IXml;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


public class XmlDom4jImpl implements IXml {

	@Override
	public void createXml(List<User> list, String fileName) throws Exception {
		Document document = DocumentHelper.createDocument();
		
		Element users = document.addElement("users");
		for (User item : list) {
			Element user = users.addElement("user");
			user.addAttribute("id", String.valueOf(item.getId()));
			
			Element name = user.addElement("name");
			name.setText(item.getName());
			Element age = user.addElement("age");
			age.setText(String.valueOf(item.getAge()));
			Element sex = user.addElement("sex");
			sex.setText(item.getSex());
		}

		XMLWriter out = null;
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			// File转换为Writer
			File xmlFile = new File(fileName); // 输出xml的路径
			fos = new FileOutputStream(xmlFile);
			osw = new OutputStreamWriter(fos, "UTF-8"); // 指定编码，防止写中文乱码
			bw = new BufferedWriter(osw);

			// 对xml输出格式化
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");
			// 生成文件
			out = new XMLWriter(bw, format);
			out.write(document);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (bw != null) {
					bw.close();
				}
				if (osw != null) {
					osw.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> parserXml(String fileName) throws Exception {
		List<User> list = new ArrayList<User>();
		
		// 读取xml文件
		SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File(fileName));
        
        // 获取根元素users
        Element content = document.getRootElement();
        		
        // 获取user列表
		List<Element> usersList = content.elements("user");
        for (Element userItem : usersList) {
        	User user = new User();
        	user.setId(Integer.valueOf(userItem.attributeValue("id")));
        	
        	List<Element> userContentList = userItem.elements();
        	for (Element userContentItem : userContentList) {
        		if ("name".equals(userContentItem.getName())) {
    				user.setName(userContentItem.getStringValue());
    			} else if ("age".equals(userContentItem.getName())) {
    				user.setAge(Integer.valueOf(userContentItem.getTextTrim()));
    			} else if ("sex".equals(userContentItem.getName())) {
    				user.setSex(userContentItem.getStringValue());
    			}
			}
			list.add(user);
		}
	
		return list;
	}

}
