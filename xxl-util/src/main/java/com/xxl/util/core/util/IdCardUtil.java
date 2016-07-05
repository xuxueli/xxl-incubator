package com.xxl.util.core.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 身份证.Util
 * @author xuxueli 2015-6-11 13:30:37
 */
public class IdCardUtil {
	private static Logger logger = LoggerFactory.getLogger(IdCardUtil.class);
	
    /**
     * 获得出生日期
     * 
     * @param idcard
     * @return
     * @throws ParseException 
     */
    public static Date getBirthDate(String idcard) {
        String year;
        String month;
        String day;
        if (idcard.length() == 18) { 
        	// 处理18位身份证
            year = idcard.substring(6, 10);
            month = idcard.substring(10, 12);
            day = idcard.substring(12, 14);
        } else { 
        	// 处理非18位身份证
            year = idcard.substring(6, 8);
            month = idcard.substring(8, 10);
            day = idcard.substring(10, 12);
            year = "19" + year;
        }
        try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
		} catch (ParseException e) {
			logger.info("{}", e);
		}
        return null;
    }

    public static void main(String[] args) {
    	System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(getBirthDate("412326200802201234")));
    }
    
}