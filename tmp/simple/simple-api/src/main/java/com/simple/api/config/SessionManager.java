package com.simple.api.config;

import com.simple.api.common.SessionStore;
import org.springframework.stereotype.Component;

/**
 * Session 管理器
 * 封装 SessionStore 的高层操作，供 Controller 和 Interceptor 使用
 * 提供创建、校验、销毁 Session 的统一入口
 */
@Component
public class SessionManager {

    private final SessionStore sessionStore;

    /** 通过构造器注入 SessionStore */
    public SessionManager(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    /**
     * 创建新 Session（登录成功后调用）
     *
     * @param username 用户名
     * @return 生成的 SessionId
     */
    public String createSession(String username) {
        return sessionStore.createSession(username);
    }

    /**
     * 校验 Session 是否有效
     * 校验通过时会刷新最后访问时间，保持 Session 活跃
     *
     * @param sessionId SessionId
     * @return 关联的用户名，Session 无效时返回 null
     */
    public String validateSession(String sessionId) {
        SessionStore.SessionInfo info = sessionStore.getSession(sessionId);
        if (info == null) {
            return null;
        }
        // 刷新最后访问时间，延长 Session 生命周期
        sessionStore.refreshLastAccess(sessionId);
        return info.getUsername();
    }

    /**
     * 根据 SessionId 获取关联的用户名（不刷新最后访问时间）
     * 用于状态查询场景（如 /api/auth/me）
     *
     * @param sessionId SessionId
     * @return 用户名，Session 无效时返回 null
     */
    public String getUsername(String sessionId) {
        SessionStore.SessionInfo info = sessionStore.getSession(sessionId);
        return info != null ? info.getUsername() : null;
    }

    /**
     * 销毁 Session（登出时调用）
     *
     * @param sessionId SessionId
     */
    public void destroySession(String sessionId) {
        sessionStore.invalidateSession(sessionId);
    }
}
