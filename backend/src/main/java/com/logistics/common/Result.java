package com.logistics.common;

import lombok.Data;

/**
 * 统一响应结果
 */
@Data
public class Result<T> {

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_ERROR = 500;

    private Integer code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return build(CODE_SUCCESS, "操作成功", null);
    }

    public static <T> Result<T> success(T data) {
        return build(CODE_SUCCESS, "操作成功", data);
    }

    public static <T> Result<T> error(String msg) {
        return build(CODE_ERROR, msg, null);
    }

    public static <T> Result<T> unauthorized(String msg) {
        return build(CODE_UNAUTHORIZED, msg, null);
    }

    public static <T> Result<T> build(int code, String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
