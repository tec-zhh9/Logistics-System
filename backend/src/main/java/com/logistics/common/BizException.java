package com.logistics.common;

/**
 * 业务异常，由 GlobalExceptionHandler 统一转换为错误提示
 */
public class BizException extends RuntimeException {

    public BizException(String message) {
        super(message);
    }
}
