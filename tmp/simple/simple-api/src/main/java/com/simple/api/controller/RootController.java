package com.simple.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 根路径控制器
 * 提供后端服务根路径的健康检查接口
 * 访问 http://localhost:8080/ 可确认服务是否正常运行
 */
@RestController
public class RootController {

    /**
     * GET /
     * 返回服务基本信息
     */
    @GetMapping("/")
    public String index() {
        return "Simple API is running. Available endpoints: "
                + "/api/auth/login, /api/user/list, /api/product/list";
    }
}
