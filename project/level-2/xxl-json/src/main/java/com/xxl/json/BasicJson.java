package com.xxl.json;

import com.xxl.json.core.BasicJsonReader;
import com.xxl.json.core.BasicJsonwriter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuxueli 2018-11-30
 */
public class BasicJson {

    private static final BasicJsonReader basicJsonReader = new BasicJsonReader();
    private static final BasicJsonwriter basicJsonwriter = new BasicJsonwriter();

    /**
     * object to json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return basicJsonwriter.toJson(object);
    }

    /**
     * json to List<Object>
     *
     * @param json
     * @return
     */
    public static List<Object> parseList(String json) {
        return basicJsonReader.parseList(json);
    }

    /**
     * json to Map<String, Object>
     *
     * @param json
     * @return
     */
    public static Map<String, Object> parseMap(String json) {
        return basicJsonReader.parseMap(json);
    }

    public static void main(String[] args) {
        Map<String, Object> result = new HashMap<>();
        result.put("int", 200);
        result.put("str", "success");
        result.put("arr", Arrays.asList("111","222"));
        result.put("float", 1.11f);

        String json = toJson(result);
        System.out.println(json);

        Object objectMap = parseMap(json);
        System.out.println(objectMap);
    }

}
