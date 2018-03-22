package com.yunjing.zuul.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/2/28
 * @description
 **/
@Data
public class ResourceDto implements Serializable {

    /**
     * 链接地址
     */
    private String uri;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源描述
     */
    private String description;

    /**
     * 请求方法
     */
    private String method;
}
