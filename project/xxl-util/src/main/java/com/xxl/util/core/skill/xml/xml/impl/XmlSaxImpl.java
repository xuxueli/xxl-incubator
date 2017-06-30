package com.xxl.util.core.skill.xml.xml.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import com.xxl.util.core.skill.xml.model.User;
import com.xxl.util.core.skill.xml.xml.IXml;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

/**
 * sax：JDK自带API
 */
public class XmlSaxImpl implements IXml {

	@Override
	public void createXml(List<User> list, String fileName) throws Exception {
		Result resultXml = new StreamResult(new FileOutputStream(fileName)); // 输出到person.xml
		SAXTransformerFactory sff = (SAXTransformerFactory) SAXTransformerFactory
				.newInstance();
		TransformerHandler th = sff.newTransformerHandler();
		th.setResult(resultXml);
		Transformer transformer = th.getTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); // 编码格式是UTF-8
		transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 换行

		// 开始xml文档
		th.startDocument();

		th.startElement("", "", "users", null); // 定义users节点
		for (User user : list) {
			AttributesImpl attrs = new AttributesImpl();
			attrs.addAttribute("", "", "id", "", String.valueOf(user.getId()));

			th.startElement("", "", "user", attrs); // 定义user节点

			// 定义name节点
			th.startElement("", "", "name", null);
			th.characters(user.getName().toCharArray(), 0, user.getName()
					.length());
			th.endElement("", "", "name");
			// 定义age节点
			th.startElement("", "", "age", null);
			th.characters(String.valueOf(user.getAge()).toCharArray(), 0,
					String.valueOf(user.getAge()).toCharArray().length);
			th.endElement("", "", "age");
			// 定义sex节点
			th.startElement("", "", "sex", null);
			th.characters(user.getSex().toCharArray(), 0, user.getSex()
					.length());
			th.endElement("", "", "sex");

			th.endElement("", "", "user"); // 结束user节点
		}
		th.endElement("", "", "users"); // 结束users节点

		// 结束xml文档
		th.endDocument();
	}

	@Override
	public List<User> parserXml(String fileName) throws Exception {
		// 加载资源文件 转化为一个输入流
		InputStream stream = new FileInputStream(fileName);
		
		// 
		SaxParseXml parseXml = new SaxParseXml();
		
		// 构建SAXParser, 调用parse()方法
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		parser.parse(stream, parseXml);

		List<User> list = parseXml.getList();
		return list;
	}

}


class SaxParseXml extends DefaultHandler{

    // 存放遍历集合
    private List<User> list;
    // 构建Student对象
    private User user;
    // 用来存放每次遍历后的元素名称(节点名称)
    private String tagName;
    
    public List<User> getList() {
		return list;
	}
	public void setList(List<User> list) {
		this.list = list;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	//只调用一次  初始化list集合  
    @Override
    public void startDocument() throws SAXException {
        list = new ArrayList<User>();
    }
    
    //调用多次    开始解析
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if(qName.equals("user")){
        	user=new User();
            //获取student节点上的id属性值
        	user.setId(Integer.parseInt(attributes.getValue(0)));
        }
        this.tagName = qName;
    }
    
    //调用多次  
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("user")){
            this.list.add(this.user);
        }
        this.tagName=null;
    }
    
    //只调用一次
    @Override
    public void endDocument() throws SAXException {
    }
    
    //调用多次
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(this.tagName!=null){
            String date=new String(ch,start,length);
            if(this.tagName.equals("name")){
                this.user.setName(date);
            }
            else if(this.tagName.equals("age")){
            	this.user.setAge(Integer.valueOf(date));
            }
            else if(this.tagName.equals("sex")){
                this.user.setSex(date);
            }
        }
    }
    
}