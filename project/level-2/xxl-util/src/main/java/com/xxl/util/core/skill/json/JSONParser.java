package com.xxl.util.core.skill.json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 语法分析器
 */
public class JSONParser {

    /**
     * parse object
     *
     * @param jsonTokenizer
     * @return  map、list-map、basic data
     * @throws Exception
     */
    public static Object parserObject(JSONTokenizer jsonTokenizer) throws Exception {
        JSONTokenizer.Token token = jsonTokenizer.get();
        try {
            if (token.type == JSONTokenizer.TokenType.ObjectStart) {
                return parserObjectToMap(jsonTokenizer);
            } else if (token.type == JSONTokenizer.TokenType.ArrayStart) {
                return parserArray(jsonTokenizer);
            } else if (token.type == JSONTokenizer.TokenType.Boolean) {
                return Boolean.valueOf(token.value);
            } else if (token.type == JSONTokenizer.TokenType.String) {
                return token.value;
            } else if (token.type == JSONTokenizer.TokenType.Number) {
                return token.value;
            } else if (token.type == JSONTokenizer.TokenType.Null) {
                return null;
            }
            throw new Exception("parse json object fail");
        } catch (Exception e) {
            throw e;
        } finally {
            // after object/array start-token, need skip end-token; other token type, need skip self
            jsonTokenizer.next();
        }
    }

    /**
     * parse object to map
     *
     * @param jsonTokenizer
     * @return
     * @throws Exception
     */
    public static Map<String, Object> parserObjectToMap(JSONTokenizer jsonTokenizer) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        JSONTokenizer.Token token = null;
        while (true) {
            token = jsonTokenizer.get();
            if (token.type == JSONTokenizer.TokenType.ObjectEnd)
                break;
            if (token.type == JSONTokenizer.TokenType.ObjectStart) {
                jsonTokenizer.next();
                continue;
            }
            if (token.type == JSONTokenizer.TokenType.Comma) {
                jsonTokenizer.next();
                continue;
            }
            String key = token.value;
            token = jsonTokenizer.next();
            if (token.type != JSONTokenizer.TokenType.Colon) {
                throw new Exception("parse json map fail");
            }
            jsonTokenizer.next();
            map.put(key, parserObject(jsonTokenizer));
        }
        return map;
    }

    /**
     * parse array object
     *
     * @param jsonTokenizer
     * @return
     * @throws Exception
     */
    public static List<Object> parserArray(JSONTokenizer jsonTokenizer) throws Exception {
        List<Object> list = new LinkedList<Object>();
        JSONTokenizer.Token token = null;
        while (true) {
            token = jsonTokenizer.get();
            if (token.type == JSONTokenizer.TokenType.ArrayEnd)
                break;
            if (token.type == JSONTokenizer.TokenType.ArrayStart) {
                jsonTokenizer.next();
                continue;
            }
            if (token.type == JSONTokenizer.TokenType.Comma) {
                jsonTokenizer.next();
                continue;
            }
            list.add(parserObject(jsonTokenizer));
        }
        return list;
    }


}