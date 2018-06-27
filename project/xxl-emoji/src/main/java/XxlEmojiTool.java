import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XxlEmojiTool {
    private static Logger logger = LoggerFactory.getLogger(XxlEmojiTool.class);

    public static char oneBegin = 0xD800;
    public static char oneEnd = 0xDBFF;
    public static char twoBegin = 0xDC00;
    public static char twoEnd = 0xDFFF;

    public static String tranferSpecialStr(String str){
        try {
            int i = 0;
            char[] chars = str.toCharArray();
            for (; i < chars.length; ) {
                char curChar = chars[i];
                if (curChar >=oneBegin && curChar <= oneEnd) {
                    i += 1;
                    char afterChar = chars[i];
                    // bwtween this，mean 4 Byte
                    if (afterChar >= twoBegin && afterChar <= twoEnd) {
                        char[] chars1 = new char[2];
                        chars1[0] = curChar;
                        chars1[1] = afterChar;
                        String replaceOldStr = new String(chars1);
                        String replaceNewStr = tranfer(curChar, afterChar);
                        str = str.replace(replaceOldStr, replaceNewStr);
                    }
                }
                i += 1;
            }
        }catch (Exception e){
            logger.error(e.getMessage(), e);
        }
        return str;
    }

    private static String tranfer(char c1, char c2){
        int all = (c1-oneBegin)*0x400+(c2-twoBegin)+0x10000;//返回十进制的数据
        return "&#"+all+";";
    }





}
