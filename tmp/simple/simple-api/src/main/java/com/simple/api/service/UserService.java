package com.simple.api.service;

import com.simple.api.entity.User;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户服务
 * 使用 ConcurrentHashMap 模拟数据库存储，实现用户增删改查
 * 服务启动时预置演示数据
 */
@Service
public class UserService {

    /**
     * 用户数据存储容器
     * Key: 用户ID（UUID）
     * Value: 用户实体
     */
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    /**
     * 服务初始化时预置演示数据
     * 包含 admin 账号和两个示例用户
     */
    @PostConstruct
    public void init() {
        // 预置 admin 账号（与 AuthService 中的演示账号一致）
        User admin = new User();
        admin.setId("admin-001");
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setEmail("admin@simple.com");
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        userStore.put(admin.getId(), admin);

        // 预置示例用户 1
        User user1 = new User();
        user1.setId(UUID.randomUUID().toString());
        user1.setUsername("zhangsan");
        user1.setPassword("123456");
        user1.setEmail("zhangsan@simple.com");
        user1.setCreateTime(LocalDateTime.now());
        user1.setUpdateTime(LocalDateTime.now());
        userStore.put(user1.getId(), user1);

        // 预置示例用户 2
        User user2 = new User();
        user2.setId(UUID.randomUUID().toString());
        user2.setUsername("lisi");
        user2.setPassword("123456");
        user2.setEmail("lisi@simple.com");
        user2.setCreateTime(LocalDateTime.now());
        user2.setUpdateTime(LocalDateTime.now());
        userStore.put(user2.getId(), user2);
    }

    /**
     * 查询所有用户列表
     */
    public List<User> list() {
        return new ArrayList<>(userStore.values());
    }

    /**
     * 根据 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户实体，不存在时抛出异常
     */
    public User getById(String id) {
        User user = userStore.get(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在：" + id);
        }
        return user;
    }

    /**
     * 创建新用户
     * 自动生成 UUID 作为 ID，并填充创建时间
     *
     * @param user 用户信息（不含 ID 和时间字段）
     * @return 创建完成后的用户实体
     */
    public User create(User user) {
        String id = UUID.randomUUID().toString();
        user.setId(id);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userStore.put(id, user);
        return user;
    }

    /**
     * 更新用户信息
     * 只能更新 username、password、email 字段，ID 和时间戳不可修改
     *
     * @param id   用户ID
     * @param user 新的用户信息
     * @return 更新后的用户实体
     */
    public User update(String id, User user) {
        User existing = getById(id);
        // 更新可变字段
        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        // 如果前端传了新密码则更新，否则保留原密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(user.getPassword());
        }
        existing.setUpdateTime(LocalDateTime.now());
        return existing;
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    public void delete(String id) {
        if (userStore.remove(id) == null) {
            throw new IllegalArgumentException("用户不存在：" + id);
        }
    }
}
