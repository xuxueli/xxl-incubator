package com.xxl.util.core.util;


import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Jackson util
 * 
 * 1、obj need private and set/get；
 * 2、do not support inner class；
 * 
 * @author xuxueli 2015-9-25 18:02:56
 */
public class JacksonUtil {
	private static Logger logger = LoggerFactory.getLogger(JacksonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    /**
     * bean、array、List、Map --> json
     * 
     * @param obj
     * @return
     * @throws Exception
     */
    public static String writeValueAsString(Object obj) {
    	try {
			return getInstance().writeValueAsString(obj);
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
			return getInstance().readValue(jsonStr, clazz);
		} catch (JsonParseException e) {
			logger.error("", e);
		} catch (JsonMappingException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
    	return null;
    }
    public static <T> T readValueRefer(String jsonStr, TypeReference typeReference) {
    	try {
			return getInstance().readValue(jsonStr, typeReference);	// new TypeReference<T>() { }	// new TypeReference<List<DemoDTO>>() { }
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
			String json = writeValueAsString(map);

			logger.error(json);
			logger.error(readValue(json, Map.class).toString());
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}
