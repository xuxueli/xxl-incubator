package com.xxl.util.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程本地变量Util
 *
 * Created by xuxueli on 17/2/9.
 */
public class ThreadLocalParamMapUtil {

    private static ThreadLocal<Map<String, Object>> chainThreadParamMap = new ThreadLocal<Map<String, Object>>();

    public static Object getChainThreadParamValue(String key) {
        Map<String, Object> paramMap = chainThreadParamMap.get();
        if (paramMap!=null) {
            return paramMap.get(key);
        }
        return null;
    }

    public static void putChainThreadParam(String key, Object value) {
        Map<String, Object> paramMap = chainThreadParamMap.get();
        if (paramMap == null) {
            paramMap = new HashMap<String, Object>();
        }
        paramMap.put(key, value);
        chainThreadParamMap.set(paramMap);
    }

    public static void removeChainThreadParam(){
        // jdk1.3之后，数据存储在线程内部，除非线程一直不销毁，否则通常不需要remove
        chainThreadParamMap.remove();
    }

}