package com.simple.api.service;

import com.simple.api.config.SessionManager;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 * 处理登录和登出逻辑
 *
 * 登录验证：使用硬编码的演示账号 admin / admin123
 * 生产环境应查询数据库并验证加密后的密码
 */
@Service
public class AuthService {

    private final SessionManager sessionManager;

    /** 演示用用户名，生产环境应从数据库查询 */
    private static final String DEMO_USERNAME = "admin";

    /** 演示用密码，生产环境应使用 BCrypt 等加密存储 */
    private static final String DEMO_PASSWORD = "admin123";

    public AuthService(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    /**
     * 用户登录
     * 校验用户名密码，通过后创建 Session 并返回 SessionId
     *
     * @param username 用户名
     * @param password 密码
     * @return SessionId，登录失败时抛出 IllegalArgumentException
     */
    public String login(String username, String password) {
        // 校验用户名和密码
        if (!DEMO_USERNAME.equals(username) || !DEMO_PASSWORD.equals(password)) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        // 创建 Session 并返回 SessionId（前端通过 Cookie 接收）
        return sessionManager.createSession(username);
    }

    /**
     * 用户登出
     * 销毁指定 SessionId 对应的 Session
     *
     * @param sessionId SessionId
     */
    public void logout(String sessionId) {
        if (sessionId != null) {
            sessionManager.destroySession(sessionId);
        }
    }

    /**
     * 根据 SessionId 获取关联的用户名（不刷新最后访问时间）
     * 用于 /api/auth/me 接口，前端刷新页面时恢复登录状态
     *
     * @param sessionId SessionId
     * @return 用户名，Session 无效时返回 null
     */
    public String getCurrentUsername(String sessionId) {
        return sessionManager.getUsername(sessionId);
    }

    /**
     * 获取演示账号的用户名
     */
    public static String getDemoUsername() {
        return DEMO_USERNAME;
    }
}
