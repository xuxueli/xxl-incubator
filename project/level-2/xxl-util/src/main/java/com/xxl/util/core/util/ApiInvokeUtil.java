package com.xxl.util.core.util;

import com.xxl.util.core.skill.flowcontrol.ReturnT;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *	对象方式请求API接口，UTIL
 *
<pre>
	 @RequestMapping(value=ApiInvokeUtil.REGISTRY, method = RequestMethod.POST, consumes = "application/json")
	 @ResponseBody
	 public ReturnT<String> registry(@RequestBody RegistryParam registryParam){
		 int ret = xxlJobRegistryDao.registryUpdate(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
		 if (ret < 1) {
			xxlJobRegistryDao.registrySave(registryParam.getRegistGroup(), registryParam.getRegistryKey(), registryParam.getRegistryValue());
		 }
		 return ReturnT.SUCCESS;
	 }
</pre>

<pre>
	 public static void main(String[] args) {
		 RegistryParam registryParam = new RegistryParam(RegistryConfig.RegistType.EXECUTOR.name(), "aaa", "112312312312");

		 ApiInvokeUtil.init("http://localhost:8080/xxl-job-admin");
		 ReturnT<String> registryResult = ApiInvokeUtil.callApiFailover(ApiInvokeUtil.REGISTRY, registryParam);

		 System.out.println(registryResult);
	 }
</pre>
 *
 * @author xuxueli 2017-05-10 21:28:15
 */
public class ApiInvokeUtil {
	private static Logger logger = LoggerFactory.getLogger(ApiInvokeUtil.class);

	public static final String CALLBACK = "/api/callback";
	public static final String REGISTRY = "/api/registry";

	private static List<String> adminAddressList = null;
	public static void init(String adminAddresses){
		// admin assress list
		if (adminAddresses != null) {
			Set<String> adminAddressSet = new HashSet<String>();
			for (String adminAddressItem: adminAddresses.split(",")) {
				if (adminAddressItem.trim().length()>0) {
					adminAddressSet.add(adminAddressItem);
				}
			}
            adminAddressList = new ArrayList<String>(adminAddressSet);
		}
	}
	public static boolean allowCallApi(){
        boolean allowCallApi = (adminAddressList!=null && adminAddressList.size()>0);
        return allowCallApi;
    }

	public static ReturnT<String> callApiFailover(String subUrl, Object requestObj) throws Exception {

		if (!allowCallApi()) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "allowCallApi fail.");
		}

		for (String adminAddress: adminAddressList) {
			ReturnT<String> registryResult = null;
			try {
				String apiUrl = adminAddress.concat(subUrl);
				registryResult = callApi(apiUrl, requestObj);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			if (registryResult!=null && registryResult.getCode()==ReturnT.SUCCESS_CODE) {
				return ReturnT.SUCCESS;
			}
		}
		return ReturnT.FAIL;
	}

	private static ReturnT<String> callApi(String finalUrl, Object requestObj) throws Exception {
		HttpPost httpPost = new HttpPost(finalUrl);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		try {

			// timeout
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(10000)
					.setSocketTimeout(10000)
					.setConnectTimeout(10000)
					.build();

			httpPost.setConfig(requestConfig);

			// data
			if (requestObj != null) {
				String json = JacksonUtil.writeValueAsString(requestObj);

				StringEntity entity = new StringEntity(json, "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");

				httpPost.setEntity(entity);
			}

			// do post
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				String responseMsg = EntityUtils.toString(entity, "UTF-8");
				if (response.getStatusLine().getStatusCode() != 200) {
					EntityUtils.consume(entity);
					return new ReturnT<String>(response.getStatusLine().getStatusCode(),
							"StatusCode（+"+ response.getStatusLine().getStatusCode() +"） Error，response：" + responseMsg);
				}

				EntityUtils.consume(entity);
				if (responseMsg!=null && responseMsg.startsWith("{")) {
					ReturnT<String> result = JacksonUtil.readValue(responseMsg, ReturnT.class);
					return result;
				}
			}
			return ReturnT.FAIL;
		} catch (Exception e) {
			logger.error("", e);
			return new ReturnT<String>(ReturnT.FAIL_CODE, e.getMessage());
		} finally {
			if (httpPost!=null) {
				httpPost.releaseConnection();
			}
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
