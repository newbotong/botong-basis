package com.yunjing.mommon.global.exception;

import com.yunjing.mommon.constant.StatusCode;

public class BaseRuntimeException extends RuntimeException {

    private final int code;

    public BaseRuntimeException(StatusCode statusCode) {
        this(statusCode.getStatusCode(), statusCode.getStatusMessage());
    }

    public BaseRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
