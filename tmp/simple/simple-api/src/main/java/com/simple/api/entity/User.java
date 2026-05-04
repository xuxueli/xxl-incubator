package com.simple.api.entity;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 使用 String 类型的 UUID 作为主键，避免多个 HashMap 之间的 ID 冲突
 */
public class User {

    /** 用户唯一标识，使用 UUID 生成 */
    private String id;

    /** 用户名，登录凭证 */
    private String username;

    /** 密码，演示项目明文存储，生产环境必须加密 */
    private String password;

    /** 邮箱地址 */
    private String email;

    /** 创建时间，由服务端自动填充 */
    private LocalDateTime createTime;

    /** 最后修改时间，每次更新时刷新 */
    private LocalDateTime updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

}
