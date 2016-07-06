package com.xxl.util.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Map;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

/**
 * HTML模板.Util
 * @author xuxueli
 */
public class HtmlTemplateUtil  {
	
	private static FreeMarkerConfigurer freemarkerConfig;
	
	public static FreeMarkerConfigurer loadConfig(){
		if (freemarkerConfig == null) {
			freemarkerConfig = (FreeMarkerConfigurer) SpringContentAwareUtil.getBeanByName("freemarkerConfig");
		}
		return freemarkerConfig;
	}
	
	/**
	 * generate static model
	 * @param packageName
	 * @return
	 */
	public static TemplateHashModel generateStaticModel(String packageName) {
		try {
			BeansWrapper wrapper = BeansWrapper.getDefaultInstance();  
			TemplateHashModel staticModels = wrapper.getStaticModels();
			TemplateHashModel fileStatics = (TemplateHashModel) staticModels.get(packageName);
			return fileStatics;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成HTML字符串
	 * 
	 * @param freeMarkerConfig	: FreeMarker配置
	 * @param content			: 页面传参
	 * @param templatePathName	: 模板名称路径,相对于模板目录
	 */
	public static String generateString(Map<?, ?> content, String templateName) {
		String htmlText = "";
		try {
			// 通过指定模板名获取FreeMarker模板实例
			Template tpl = loadConfig().getConfiguration().getTemplate(templateName);
			htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(tpl, content);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlText;
	}

	public static void main(String[] args) {
		String temp = generateString(null, "hello.ftl");
		System.out.println(temp);
	}
	
	/**
	 * 生成HTML文件
	 * 
	 * @param freeMarkerConfig	: FreeMarker配置
	 * @param content			: 页面传参
	 * @param templatePathName	: 模板名称路径,相对于模板目录
	 * @param filePathName		: 文件名称路径,相对于项目跟目录
	 * DEMO:HtmlTemplateUtil.generate(freemarkerConfig, new HashMap<String, Object>(), "net/index.ftl", "/index.html");
	 */
	public static void generateFile(Map<?, ?> content, String templatePathName, String filePathName) {
		Writer out = null;
		try {
			// mkdirs
			File htmlFile = new File(WebPathUtil.webPath() + filePathName);
            if (!htmlFile.getParentFile().exists()) {
                htmlFile.getParentFile().mkdirs();
            }
            // process
            Template template = loadConfig().getConfiguration().getTemplate(templatePathName);
			out = new OutputStreamWriter(new FileOutputStream(WebPathUtil.webPath() + filePathName), "UTF-8");
			template.process(content, out);
			out.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
					out = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
