package com.xxl.search.client.util;


import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Jackson util
 * 1、obj need private and set/get； 2、do not support inner class；
 * @author xuxueli 2015-9-25 18:02:56
 */
public class JacksonUtil {
	private static Logger logger = LogManager.getLogger();
	private final static ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * bean、array、List、Map --> json
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String writeValueAsString(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	/**
	 * string --> bean、Map、List(array)
	 *
	 * @param jsonStr
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static <T> T readValue(String jsonStr, Class<T> clazz) {
		try {
			return objectMapper.readValue(jsonStr, clazz);
		} catch (JsonParseException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	public static <T> T readValueRefer(String jsonStr, Class<T> clazz) {
		try {
			return objectMapper.readValue(jsonStr, new TypeReference<T>() {
			});
		} catch (JsonParseException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}

	public static void main(String[] args) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("aaa", "111");
			map.put("bbb", "222");

			logger.info(writeValueAsString(map));
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
