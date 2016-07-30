package com.xxl.util.core.skill.crawler.htmlparser.downpage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 下载 url 指向的网页
 * @author xuxueli
 */
public class PageDownLoader {
	private static Logger logger = LoggerFactory.getLogger(PageDownLoader.class);

	/**
	 * 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
	 * @param url
	 * @param contentType
	 * @return
	 */
	private String getFileNameByUrl(String url, String contentType) {
		url = url.substring(7);
		if (contentType.indexOf("html") != -1) {
			// text/html
			url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
			return url;
		} else {
			// 如application/pdf
			return url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}

	/**
	 * 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
	 * @param data
	 * @param filePath
	 */
	private void saveToLocal(byte[] data, String filePath) {
		try {
			DataOutputStream out = new DataOutputStream(new FileOutputStream(new File(filePath)));
			for (int i = 0; i < data.length; i++){
				out.write(data[i]);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			logger.error("", e);
		}
	}

	/**
	 * 下载 url 指向的网页
	 * @param url
	 * @return
	 */
	public String downloadFile(String url) {
		String filePath = null;
		
		//设置请求和传输超时时间
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()
				.setSocketTimeout(5000)
				.setConnectTimeout(5000)
				.build(); 
		httpGet.setConfig(requestConfig);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		// 3.执行 HTTP GET 请求
		try {
			HttpResponse response = httpClient.execute(httpGet);
			// 解析请求
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK && null != entity){
				byte[] responseBody = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
				
				// 根据 url 和网页类型生成需要保存的网页的文件名 去除掉 url 中非文件名字符
				filePath = "" + getFileNameByUrl(url,	entity.getContentType().getValue());
				
				// 保存网页字节数组到本地文件 filePath 为要保存的文件的相对地址
				saveToLocal(responseBody, filePath);
			} else {
				logger.error("Method failed: " + statusCode);
				filePath = null;
			}

			
		} catch (ClientProtocolException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} finally{
			httpGet.releaseConnection();
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return filePath;
	}

	// 测试的 main 方法
	public static void main(String[] args) {
		PageDownLoader downLoader = new PageDownLoader();
		downLoader.downloadFile("http://www.baidu.com/");
	}
}