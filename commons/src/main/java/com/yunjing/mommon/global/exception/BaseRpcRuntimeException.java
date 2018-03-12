package com.yunjing.mommon.global.exception;

import com.yunjing.mommon.constant.StatusCode;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
public class BaseRpcRuntimeException extends RuntimeException {

    private final int code;

    public BaseRpcRuntimeException(StatusCode statusCode) {
        this(statusCode.getStatusCode(), statusCode.getStatusMessage());
    }

    public BaseRpcRuntimeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
