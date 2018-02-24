package com.yunjing.mommon.model;

import java.io.Serializable;

/**
 * 响应模型
 * @version 1.0.0
 * @author: zhangx
 * @date 2018/1/20 11:13
 * @description
 **/
public class ResponseModel implements Serializable {

    /** 响应码 */
    private String code;

    /** 响应信息*/
    private String msg;

    /** 响应数据 */
    private Object data;

    public ResponseModel() {
    }

    public ResponseModel(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseModel(String code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
