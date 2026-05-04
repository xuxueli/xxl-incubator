package com.simple.api.web.session;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存 Session 存储
 * 使用 ConcurrentHashMap 实现线程安全的 Session 管理
 * 替代数据库存储，适用于演示项目
 */
@Component
public class SessionStore {

    /**
     * Session 存储容器
     * Key: SessionId（UUID 字符串）
     * Value: SessionInfo（会话信息）
     */
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();

    /**
     * 创建新 Session
     *
     * @param username 用户名
     * @return 生成的 SessionId
     */
    public String createSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        SessionInfo info = new SessionInfo(sessionId, username);
        sessions.put(sessionId, info);
        return sessionId;
    }

    /**
     * 根据 SessionId 获取会话信息
     *
     * @param sessionId SessionId
     * @return SessionInfo，不存在时返回 null
     */
    public SessionInfo getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    /**
     * 使 Session 失效（登出时调用）
     *
     * @param sessionId SessionId
     */
    public void invalidateSession(String sessionId) {
        sessions.remove(sessionId);
    }

    /**
     * 刷新 Session 的最后访问时间
     *
     * @param sessionId SessionId
     */
    public void refreshLastAccess(String sessionId) {
        SessionInfo info = sessions.get(sessionId);
        if (info != null) {
            info.setLastAccessTime(LocalDateTime.now());
        }
    }

    /**
     * 获取当前活跃 Session 数量
     */
    public int size() {
        return sessions.size();
    }

    /**
     * Session 信息内部类
     * 记录 SessionId、关联的用户名、创建时间和最后访问时间
     */
    public static class SessionInfo {
        private final String sessionId;
        private final String username;
        private final LocalDateTime createTime;
        private LocalDateTime lastAccessTime;

        public SessionInfo(String sessionId, String username) {
            this.sessionId = sessionId;
            this.username = username;
            this.createTime = LocalDateTime.now();
            this.lastAccessTime = this.createTime;
        }

        public String getSessionId() {
            return sessionId;
        }

        public String getUsername() {
            return username;
        }

        public LocalDateTime getCreateTime() {
            return createTime;
        }

        public LocalDateTime getLastAccessTime() {
            return lastAccessTime;
        }

        public void setLastAccessTime(LocalDateTime lastAccessTime) {
            this.lastAccessTime = lastAccessTime;
        }
    }
}
