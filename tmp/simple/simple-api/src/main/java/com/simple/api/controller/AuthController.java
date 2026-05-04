package com.simple.api.controller;

import com.simple.api.common.Result;
import com.simple.api.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 处理登录和登出请求
 *
 * 登录成功后通过 Set-Cookie 响应头下发 SessionId
 * 登出时使 Cookie 失效并销毁 Session
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户登录
     * POST /api/auth/login
     *
     * 请求体：{ "username": "admin", "password": "admin123" }
     * 响应：{ "code": 0, "message": "操作成功", "data": { "username": "admin" } }
     * 响应头：Set-Cookie: SIMPLE_SESSION_ID=<uuid>; Path=/; HttpOnly
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginRequest request,
                                             HttpServletResponse response) {
        // 校验并创建 Session，返回 SessionId
        String sessionId = authService.login(request.getUsername(), request.getPassword());

        // 通过 Set-Cookie 响应头将 SessionId 写入浏览器 Cookie
        Cookie cookie = new Cookie("SIMPLE_SESSION_ID", sessionId);
        cookie.setPath("/");             // Cookie 作用路径为全站
        cookie.setHttpOnly(true);        // 禁止 JS 读取，防止 XSS 攻击
        cookie.setMaxAge(86400);         // 有效期 24 小时
        cookie.setSecure(false);         // 开发环境使用 HTTP，生产环境设为 true
        response.addCookie(cookie);

        // 返回当前登录用户信息（不返回密码和 SessionId）
        Map<String, Object> data = new HashMap<>();
        data.put("username", request.getUsername());
        return Result.success(data);
    }

    /**
     * 获取当前登录用户信息
     * GET /api/auth/me
     *
     * 前端刷新页面时调用，根据 Cookie 中的 Session 恢复登录状态
     * 返回 200 表示 Session 有效，返回 401 表示未登录
     */
    @GetMapping("/me")
    public Result<Map<String, Object>> me(
            jakarta.servlet.http.HttpServletRequest request) {
        // 从 Cookie 提取 SessionId 并校验
        String sessionId = extractSessionId(request);
        if (sessionId == null) {
            return Result.error(401, "未登录");
        }
        // 返回当前用户名
        String username = authService.getCurrentUsername(sessionId);
        if (username == null) {
            return Result.error(401, "登录已过期");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        return Result.success(data);
    }

    /**
     * 用户登出
     * POST /api/auth/logout
     *
     * 销毁 Session 并使 Cookie 失效
     * 请求必须携带有效的 SIMPLE_SESSION_ID Cookie
     */
    @PostMapping("/logout")
    public Result<Void> logout(jakarta.servlet.http.HttpServletRequest request,
                               HttpServletResponse response) {
        // 从 Cookie 中提取 SessionId
        String sessionId = extractSessionId(request);
        // 销毁 Session
        authService.logout(sessionId);

        // 设置 Cookie 过期，浏览器会自动清除
        Cookie cookie = new Cookie("SIMPLE_SESSION_ID", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);  // 立即过期
        response.addCookie(cookie);

        return Result.success();
    }

    /**
     * 从请求的 Cookie 中提取 SIMPLE_SESSION_ID
     */
    private String extractSessionId(jakarta.servlet.http.HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("SIMPLE_SESSION_ID".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * 登录请求体 DTO
     */
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
