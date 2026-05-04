package com.simple.api.controller;

import com.simple.api.entity.common.Result;
import com.simple.api.entity.User;
import com.simple.api.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * 提供用户增删改查的 RESTful API
 *
 * 所有接口均受 AuthInterceptor 保护，需要先登录获取有效 Session Cookie
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户列表，支持按用户名搜索
     * GET /api/user/list?username=xxx&pageNum=1&pageSize=10
     */
    @GetMapping("/list")
    public Result<com.simple.api.entity.common.PageResult<User>> list(
            @RequestParam(required = false, defaultValue = "") String username,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(userService.page(username, pageNum, pageSize));
    }

    /**
     * 根据 ID 查询用户详情
     * GET /api/user/{id}
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable String id) {
        return Result.success(userService.getById(id));
    }

    /**
     * 创建新用户
     * POST /api/user
     *
     * 请求体：{ "username": "wangwu", "password": "654321", "email": "wangwu@simple.com" }
     */
    @PostMapping
    public Result<User> create(@RequestBody User user) {
        return Result.success(userService.create(user));
    }

    /**
     * 更新用户信息
     * PUT /api/user/{id}
     *
     * 请求体：{ "username": "newname", "email": "new@simple.com" }
     * 只更新提供的字段，未提供的字段保持不变
     */
    @PutMapping("/{id}")
    public Result<User> update(@PathVariable String id, @RequestBody User user) {
        return Result.success(userService.update(id, user));
    }

    /**
     * 删除用户
     * DELETE /api/user/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable String id) {
        userService.delete(id);
        return Result.success();
    }
}
