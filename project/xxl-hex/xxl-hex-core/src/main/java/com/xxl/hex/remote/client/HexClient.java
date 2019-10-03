package com.xxl.hex.remote.client;

import com.xxl.hex.handler.response.HexResponse;
import com.xxl.hex.serialise.ByteHexConverter;
import com.xxl.hex.serialise.ByteReadFactory;
import com.xxl.hex.serialise.ByteWriteFactory;
import com.xxl.hex.serialise.JacksonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * http util to send hex data
 * @author xuxueli
 * @version  2015-11-28 15:30:59
 */
public class HexClient {
	private static Logger logger = LoggerFactory.getLogger(HexClient.class);

	// ---------------------- serialize ----------------------
	/**
	 * format object >>> json >>> byte[] >>> hex
	 * @param obj
	 * @return result
	 */
	public static String formatObj2Json2Byte2Hex(Object obj){
		// obj to json
		String json = JacksonUtil.writeValueAsString(obj);

		return formatJson2Byte2Hex(json);
	}
	/**
	 * format json >>> byte[] >>> hex
	 * @param json
	 * @return
     */
	private static String formatJson2Byte2Hex(String json){
		// obj to json
		int len = ByteHexConverter.getByteLen(json);

		// json to byte[]
		ByteWriteFactory byteWriteFactory = new ByteWriteFactory(4 + len);
		byteWriteFactory.writeInt(len);
		byteWriteFactory.writeString(json, len);
		byte[] bytes = byteWriteFactory.getBytes();

		// byte to hex
		String hex = ByteHexConverter.byte2hex(bytes);
		return hex;
	}

	// ---------------------- deserialize ----------------------

	/**
	 * parse hex >>> byte >>> json >>> obj
	 * @param hex
	 * @param clazz
	 * @return result
	 */
	public static <T> T parseHex2Byte2Json2Obj(String hex, Class<T> clazz){
		String json = parseHex2Byte2Json(hex);

		// json to obj
		T obj = JacksonUtil.readValue(json, clazz);
		return obj;
	}

	/**
	 * parse hex >>> byte >>> json
	 * @param hex
     * @return
     */
	public static String parseHex2Byte2Json(String hex){
		// hex to byte[]
		byte[] bytes = ByteHexConverter.hex2Byte(hex);

		// byte[] to json
		ByteReadFactory byteReadFactory = new ByteReadFactory(bytes);
		String json = byteReadFactory.readString(byteReadFactory.readInt());
		return json;
	}

	// ---------------------- remote invoke ----------------------

	/**
	 * invoke, request-obj 2 response-obj
	 * @param url
	 * @param mapping
	 * @param request
	 * @param resopnseClass
     * @return
     */
	public static HexResponse handleObj(String url, String mapping, Object request, Class<? extends HexResponse> resopnseClass) {
		// request-obj 2 request-hex
		String request_hex = formatObj2Json2Byte2Hex(request);

		// request-hex 2 response-hex
		String response_hex = postHex(url, mapping, request_hex);

		if (request_hex == null) {
			return new HexResponse.SimpleHexResponse(HexResponse.SimpleHexResponse.CODE_FAIL, "请求失败");
		}

		HexResponse hexResponse = parseHex2Byte2Json2Obj(response_hex, resopnseClass);
		return hexResponse;
	}

	/**
	 * invoke, request-json 2 response-json
	 * @param url
	 * @param mapping
	 * @param requestJson
     * @return
     */
	public static String handleJson(String url, String mapping, String requestJson) {
		// request-json 2 request-hex
		String request_hex = formatJson2Byte2Hex(requestJson);

		// request hex 2 response hex
		String response_hex = postHex(url, mapping, request_hex);
		if (response_hex==null) {
			return JacksonUtil.writeValueAsString(new HexResponse.SimpleHexResponse(HexResponse.SimpleHexResponse.CODE_FAIL, "请求失败"));
		}

		// response-hex to response-json
		String response_json = parseHex2Byte2Json(response_hex);
		return response_json;
	}

	public static final String MAPPING = "mapping";
	public static final String HEX = "hex";
	public static final String PLAIN = "plain";
	/**
	 * invoke, request-hex 2 response-hex
	 * @param base_url
	 * @param mapping
	 * @param hex
     * @return
     */
	public static String postHex(String base_url, String mapping, String hex){

		HttpPost httpPost = null;
		CloseableHttpClient httpClient = null;
		try{
			// post param
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			formParams.add(new BasicNameValuePair(MAPPING, mapping));
			formParams.add(new BasicNameValuePair(HEX, hex));

			// config
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();

			// post
			httpPost = new HttpPost(base_url);
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			httpPost.setConfig(requestConfig);

			// execute
			httpClient = HttpClients.custom().disableAutomaticRetries().build();
			HttpResponse response = httpClient.execute(httpPost);

			// parse response
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				if (response.getStatusLine().getStatusCode() == 200) {
					String response_hex = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
					if (response_hex!=null && response_hex.trim().length()>0) {
						return response_hex.trim();
					}
				}
				EntityUtils.consume(entity);
			}
			return null;
		} catch (Exception e) {
			logger.error("", e);
		} finally{
			if (httpPost!=null) {
				httpPost.releaseConnection();
			}
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					logger.error("", e);
				}
			}
		}
		return null;
	}

}
