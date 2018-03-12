package com.yunjing.mommon.wrapper;

import com.yunjing.mommon.constant.StatusCode;

public class ResponseEntityWrapper<T> {

    private int statusCode;
    private String statusMessage;
    private T data;

    private ResponseEntityWrapper(Builder builder) {
        this.statusCode = builder.c;
        this.statusMessage = builder.m;
        this.data = (T) builder.o;
    }

    public static class Builder {

        private int c;
        private String m;
        private Object o;

        public Builder statusCode(int code) {
            this.c = code;
            return this;
        }

        public Builder statusMessage(String message) {
            this.m = message;
            return this;
        }

        public <T> Builder data(T data) {
            this.o = data;
            return this;
        }

        public <T> Builder with(StatusCode statusCode, T data) {
            return statusMessage(statusCode.getStatusMessage())
                    .statusCode(statusCode.getStatusCode())
                    .data(data);
        }

        public ResponseEntityWrapper build() {
            return new ResponseEntityWrapper(this);
        }
    }

    public static <T> ResponseEntityWrapper success(T data) {
        return new Builder().statusCode(StatusCode.SUCCESS.getStatusCode())
                .statusMessage(StatusCode.SUCCESS.getStatusMessage())
                .data(data)
                .build();
    }

    public static ResponseEntityWrapper error(int code, String message) {
        return new Builder().statusCode(code)
                .statusMessage(message)
                .build();
    }

    public static ResponseEntityWrapper error(StatusCode statusCode) {
        return error(statusCode.getStatusCode(), statusCode.getStatusMessage());
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public T getData() {
        return data;
    }
}
