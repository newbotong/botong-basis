package com.yunjing.mommon.global.exception;

import com.yunjing.mommon.constant.StatusCode;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/2/28
 * @description
 **/
public class BaseException extends Exception {

    private final int code;

    public BaseException(StatusCode statusCode) {
        this(statusCode.getStatusCode(), statusCode.getStatusMessage());
    }

    public BaseException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(String message) {
        super(message);
        this.code = StatusCode.ERROR.getStatusCode();
    }

    public int getCode() {
        return code;
    }
}
