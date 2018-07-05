package com.xxl.util.core.skill.json;

public class Test {

    public static void main(String[] args) throws Exception {

        String json = "{  \"success\": true,  \"message\": \"123\",  \"result\": [    -2146464718]}";

        JSONTokenizer JSONTokenizer = new JSONTokenizer(json);

        Object map = JSONParser.parserObject(JSONTokenizer);
        System.out.println(map);
    }
}