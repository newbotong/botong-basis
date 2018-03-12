package com.yunjing.mommon.wrapper;

import com.alibaba.fastjson.JSON;
import com.yunjing.mommon.constant.StatusCode;
import lombok.Data;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
@Data
public class RpcResponseWrapper {

    private int statusCode;
    private String statusMessage;
    private String data;

    public static <T> RpcResponseWrapper success(T data) {
        return result(StatusCode.SUCCESS, data);
    }

    public static <T> RpcResponseWrapper error(T data) {
        return result(StatusCode.ERROR, data);
    }

    public static RpcResponseWrapper error(StatusCode statusCode) {
        return result(statusCode, null);
    }

    public static RpcResponseWrapper error(int statusCode, String statusMessage) {
        return result(statusCode, statusMessage, null);
    }

    public static <T> RpcResponseWrapper result(StatusCode statusCode, T data) {
        return result(statusCode.getStatusCode(), statusCode.getStatusMessage(), data);
    }

    public static <T> RpcResponseWrapper result(int statusCode, String statusMessage, T data) {
        RpcResponseWrapper wrapper = new RpcResponseWrapper();
        wrapper.statusCode = statusCode;
        wrapper.statusMessage = statusMessage;
        if (null != data) {
            wrapper.data = JSON.toJSONString(data);
        }
        return wrapper;
    }
}
