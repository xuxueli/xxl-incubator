package com.simple.api.web.exception;

import com.simple.api.entity.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * 统一拦截 Controller 层抛出的异常，转换为标准 Result 格式返回
 * 避免前端收到非标准格式的报错响应
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理非法参数异常（如参数校验失败）
     * 返回 400 级别的业务错误
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgument(IllegalArgumentException e) {
        return Result.error(400, e.getMessage());
    }

    /**
     * 处理非法状态异常（如未登录、Session 失效）
     * 返回 401 未授权
     */
    @ExceptionHandler(IllegalStateException.class)
    public Result<Void> handleIllegalState(IllegalStateException e) {
        return Result.error(401, e.getMessage());
    }

    /**
     * 兜底处理所有未分类异常
     * 返回 500 服务器内部错误，并打印堆栈便于排查
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
}
