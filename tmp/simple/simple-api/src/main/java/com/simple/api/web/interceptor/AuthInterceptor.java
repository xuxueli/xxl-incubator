package com.simple.api.web.interceptor;

import com.simple.api.entity.common.Result;
import com.simple.api.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import tools.jackson.databind.ObjectMapper;

/**
 * 认证拦截器
 * 在每个请求到达 Controller 之前拦截，校验 Session Cookie 是否有效
 * 白名单接口（如登录接口）跳过校验
 *
 * 拦截逻辑：
 * 1. 检查请求路径是否为白名单（登录接口），是则放行
 * 2. 从 Cookie 中提取 SIMPLE_SESSION_ID
 * 3. 如果 Cookie 不存在或无效，返回 401 并阻止请求
 * 4. 如果 Session 有效，将用户名存入 request 属性，放行
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final SessionManager sessionManager;
    private final ObjectMapper objectMapper;

    /** Cookie 名称，必须与 application.yml 中的配置一致 */
    private static final String SESSION_COOKIE_NAME = "SIMPLE_SESSION_ID";

    /** 不需要认证的接口白名单 */
    private static final String LOGIN_PATH = "/api/auth/login";

    /** 获取当前用户信息（用于页面刷新恢复状态） */
    private static final String ME_PATH = "/api/auth/me";

    public AuthInterceptor(SessionManager sessionManager, ObjectMapper objectMapper) {
        this.sessionManager = sessionManager;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        // 预检请求（CORS OPTIONS）直接放行，让浏览器继续发送正式请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 白名单路径跳过认证
        String uri = request.getRequestURI();
        String method = request.getMethod();
        if (LOGIN_PATH.equals(uri) && "POST".equalsIgnoreCase(method)) {
            return true;
        }
        // /api/auth/me 也需要白名单，前端刷新页面时调用此接口恢复登录状态
        if (ME_PATH.equals(uri) && "GET".equalsIgnoreCase(method)) {
            return true;
        }

        // 从 Cookie 中提取 SessionId
        String sessionId = extractSessionId(request);
        if (sessionId == null) {
            writeUnauthorized(response, "未登录，请先登录");
            return false;
        }

        // 校验 Session 是否有效
        String username = sessionManager.validateSession(sessionId);
        if (username == null) {
            writeUnauthorized(response, "登录已过期，请重新登录");
            return false;
        }

        // 将当前登录用户名存入 request 属性，供 Controller 使用
        request.setAttribute("currentUsername", username);
        return true;
    }

    /**
     * 从请求的 Cookie 中提取 SessionId
     */
    private String extractSessionId(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 向响应写入 401 错误
     * 使用 ObjectMapper 将 Result 对象序列化为 JSON
     */
    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(401, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
