package com.simple.api.common;

import lombok.Data;

/**
 * 统一 API 响应包装类
 * 所有接口返回统一格式：{ code, message, data }
 * code = 0 表示成功，非 0 表示失败
 *
 * @param <T> 响应数据的类型
 */
@Data
public class Result<T> {

    /** 状态码：0 表示成功，其他值表示失败 */
    private int code;

    /** 提示消息 */
    private String message;

    /** 响应数据体，成功时携带实际数据 */
    private T data;

    /** 私有构造，统一通过静态工厂方法创建 */
    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功响应，无数据
     */
    public static <T> Result<T> success() {
        return new Result<>(0, "操作成功", null);
    }

    /**
     * 成功响应，携带数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(0, "操作成功", data);
    }

    /**
     * 失败响应，使用默认错误码 1
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(1, message, null);
    }

    /**
     * 失败响应，指定错误码和消息
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }
}
