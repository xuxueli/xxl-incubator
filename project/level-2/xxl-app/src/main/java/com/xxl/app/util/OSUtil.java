package com.xxl.app.util;

/**
 * Created by xuxueli on 16/8/23.
 */
public class OSUtil {

    public enum OSEnum{
        Mac, Windows, LinuxOrUnix;
    }

    public static OSEnum os(){
        //String osTitle = System.getProperty("os.title", "");
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Mac OS")) {
            return OSEnum.Mac;
        } else if (osName.startsWith("Windows")) {
            return OSEnum.Windows;
        } else {
            return OSEnum.LinuxOrUnix;
        }
    }

    public static void main(String[] args) {
        System.out.println(os());
    }

}
