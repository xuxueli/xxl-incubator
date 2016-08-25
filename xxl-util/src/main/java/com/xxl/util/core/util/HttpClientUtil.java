package com.xxl.util.core.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * http util to send data
 * 
 * @author xuxueli
 * @version 2015-11-28 15:30:59
 */
public class HttpClientUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	/**
	 * http post request
	 * @param url
	 * @param params
	 * @return	[0]=responseMsg, [1]=exceptionMsg
	 */
	public static String post(String url, Map<String, String> params){
		HttpPost httpPost = null;
		CloseableHttpClient httpClient = null;
		try{
			// httpPost config
			httpPost = new HttpPost(url);
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for(Map.Entry<String,String> entry : params.entrySet()){
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			}
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			httpPost.setConfig(requestConfig);
			
			// httpClient = HttpClients.createDefault();	// default retry 3 times
			// httpClient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).build();
			httpClient = HttpClients.custom().disableAutomaticRetries().build();
			
			// parse response
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				if (response.getStatusLine().getStatusCode() == 200) {
					String responseMsg = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
					return responseMsg;
				}
				EntityUtils.consume(entity);
			}
			logger.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			/*StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			callback.setMsg(out.toString());*/
			return e.getMessage();
		} finally{
			if (httpPost!=null) {
				httpPost.releaseConnection();
			}
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * http get request
	 * @param url
	 * @param queryString
	 * @return
	 */
	public static String get(String url, String queryString){
		// fill params
		if (queryString != null && queryString.trim().length()>0) {
			url = url + "?" + queryString;
		}

		// httpGet config
		HttpGet httpGet = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		httpGet.setConfig(requestConfig);

		CloseableHttpClient httpClient = null;
		try{
			// httpClient = HttpClients.createDefault();	// default retry 3 times
			// httpClient = HttpClients.custom().setRetryHandler(new DefaultHttpRequestRetryHandler(3, true)).build();
			httpClient = HttpClients.custom().disableAutomaticRetries().build();

			// parse response
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				if (response.getStatusLine().getStatusCode() == 200) {
					String responseMsg = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
					return responseMsg;
				}
				EntityUtils.consume(entity);
			}
			logger.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			/*StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			callback.setMsg(out.toString());*/
			return e.getMessage();
		} finally{
			if (httpGet!=null) {
				httpGet.releaseConnection();
			}
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * post request
	 */
	public static byte[] postRequestByte(String url, byte[] date) {
		byte[] responseBytes = null;

		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {
			// init post datas
			if (date != null) {
				httpPost.setEntity(new ByteArrayEntity(date, ContentType.DEFAULT_BINARY));
			}
			// do post
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				responseBytes = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.releaseConnection();
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseBytes;
	}

	/**
	 * read bytes from http request
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static final byte[] readRequestByte(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");
		int contentLen = request.getContentLength();
		InputStream is = request.getInputStream();
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					if (readLengthThisTime == -1) {
						break;
					}
					readLen += readLengthThisTime;
				}
				return message;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new byte[] {};
	}
	
	public static void main(String[] args) {
		System.out.println(get("https://www.hao123.com/", null));
	}
	
}
