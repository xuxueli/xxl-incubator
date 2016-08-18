package com.xxl.cip.util;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * browse util
 * @author xuxueli 2016-08-18 18:03:12
 */
public class BrowseUtil {

    public static void browse(String url) throws Exception {
        //String osTitle = System.getProperty("os.title", "");
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
            openURL.invoke(null, new Object[]{url});
        } else if (osName.startsWith("Windows")) {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else {
            // Unix or Linux
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String browser = null;
            for (int count = 0; count < browsers.length && browser == null; count++) {
                // 进程创建成功, ==0是表示正常结束
                if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0){
                    browser = browsers[count];
                }
            }
            if (browser == null) {
                throw new Exception("Could not find web browser");
            } else {
                Runtime.getRuntime().exec(new String[]{browser, url});
            }
        }
    }

    //主方法 测试类
    public static void main(String[] args) {
        String url = "http://iteye.blog.163.com/";
        try {
            BrowseUtil.browse(url);

            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
