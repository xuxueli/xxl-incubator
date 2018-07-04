package com.xxl.emoji.strategy.impl;

import com.xxl.emoji.strategy.EmojiParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * decimal emoji parser
 *
 * @author xuxueli 2018-07-04 20:49:45
 */
public class HtmlDecimalEmojiParser extends EmojiParser {
    private static Logger logger = LoggerFactory.getLogger(HtmlDecimalEmojiParser.class);


    public String encode(String input) {
        return emojiToDecimal(input);
    }

    public String decode(String input) {
        return null;
    }

    // ---------------------- tool ----------------------


    public static final char oneBegin = 0xD800;
    public static final char oneEnd = 0xDBFF;
    public static final char twoBegin = 0xDC00;
    public static final char twoEnd = 0xDFFF;

    /**
     * emoji to decimal(10)
     *
     * @param input
     * @return
     */
    private static String emojiToDecimal(String input) {
        try {
            int i = 0;
            char[] chars = input.toCharArray();
            for (; i < chars.length; ) {
                char curChar = chars[i];
                if (curChar >= oneBegin && curChar <= oneEnd) {
                    i += 1;
                    char afterChar = chars[i];
                    // bwtween this，mean 4 Byte
                    if (afterChar >= twoBegin && afterChar <= twoEnd) {
                        char[] chars1 = new char[2];
                        chars1[0] = curChar;
                        chars1[1] = afterChar;
                        String replaceOldStr = new String(chars1);
                        String replaceNewStr = tranfer(curChar, afterChar);
                        input = input.replace(replaceOldStr, replaceNewStr);
                    }
                }
                i += 1;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return input;
    }

    private static String tranfer(char c1, char c2) {
        int all = (c1 - oneBegin) * 0x400 + (c2 - twoBegin) + 0x10000;//返回十进制的数据
        return "&#" + all + ";";
    }


}
