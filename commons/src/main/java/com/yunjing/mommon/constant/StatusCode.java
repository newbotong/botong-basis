package com.yunjing.mommon.constant;

public enum StatusCode {
    SUCCESS(200, "成功"),
    FORBIDDEN(403, "权限失败"),
    ERROR(500, "系统错误"),

    TOKEN_NOT_AVAILABLE(200001, "AccessToken已被注销，请重新登录");

    private String message;
    private int code;

    StatusCode(int statusCode, String statusMessage) {
        this.message = statusMessage;
        this.code = statusCode;
    }

    public String getStatusMessage() {
        return message;
    }

    public int getStatusCode() {
        return code;
    }

}
