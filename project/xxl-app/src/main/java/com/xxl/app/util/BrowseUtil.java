package com.xxl.app.util;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * browse util
 * @author xuxueli 2016-08-18 18:03:12
 */
public class BrowseUtil {

    public static void browse(String url) throws Exception {

        OSUtil.OSEnum os = OSUtil.os();
        switch (os) {
            case Windows:{
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                break;
            }
            case Mac:{
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
                break;
            }
            case LinuxOrUnix:{
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
                break;
            }
        }
    }

    public static void main(String[] args) {
        try {
            BrowseUtil.browse("https://github.com/xuxueli/");
            BrowseUtil.browse("http://my.oschina.net/xuxueli");
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
