package com.xxl.util.core.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by xuxueli on 17/4/6.
 */
public class PinyinUtil {

    public static String getPinYin(String strs) {

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // WITH_TONE_NUMBER/WITHOUT_TONE/WITH_TONE_MARK
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        char[] ch = strs.trim().toCharArray();
        StringBuffer buffer = new StringBuffer("");

        try {
            for (int i = 0; i < ch.length; i++) {
                // unicode，bytes应该也可以.
                if (Character.toString(ch[i]).matches("[\u4e00-\u9fa5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ch[i], format);
                    buffer.append(temp[0]);
                    //buffer.append(" ");
                } else {
                    buffer.append(Character.toString(ch[i]));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * 获取大写首字母
     *
     * @param str
     * @return
     */
    public static String getFirstLetter(String str) {
        // trim
        if (str == null) {
            return "";
        }
        str = str.trim();

        String convert = "";

        char word = str.charAt(0);
        String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);

        if (pinyinArray != null) {
            convert += pinyinArray[0].charAt(0);
        }
        else {
            convert += word;
        }

        return convert.toUpperCase();
    }

    public static void main(String[] args) {
        String chstrs = "天儿不错6，Good homesick!";
        System.out.println("输入:" + chstrs);
        System.out.println("结果：" + getPinYin(chstrs));
    }

}
