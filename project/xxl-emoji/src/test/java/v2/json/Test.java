package v2.json;

public class Test {

    public static void main(String[] args) throws Exception {

        String json = "{  \"success\": true,  \"message\": \"123\",  \"result\": [    -2146464718]}";

        JSONTokenizer JSONTokenizer = new JSONTokenizer(json);

        Object map = JsonParser.parserObject(JSONTokenizer);
        System.out.println(map);
    }
}