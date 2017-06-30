package com.xxl.util.core.skill.xml.xml;

import com.xxl.util.core.skill.xml.model.User;

import java.util.List;


public interface IXml {
	
	public void createXml(List<User> list, String fileName) throws Exception;
	
	public List<User> parserXml(String fileName) throws Exception;
	
}
