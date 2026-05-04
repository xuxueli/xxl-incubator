package com.simple.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 * 整个后端服务的入口点
 */
@SpringBootApplication
public class SimpleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleApiApplication.class, args);
    }
}
