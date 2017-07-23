package com.xxl.util.core.util;

import javax.servlet.http.HttpServletRequest;

/**
 * get ip from request
 *
 * @author xuxueli 2017-07-23 18:52:08
 */
public class IpUtil2 {

    public static String getIp(HttpServletRequest request) {

        // X-Forwarded-For
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"unKnown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }

        // X-Real-IP
        ip = request.getHeader("X-Real-IP");
        if (ip != null && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }

        // RemoteAddr
        return request.getRemoteAddr();
    }

}
