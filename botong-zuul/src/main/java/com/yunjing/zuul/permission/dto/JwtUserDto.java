package com.yunjing.zuul.permission.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
@Data
public class JwtUserDto implements Serializable {

    private String identity;
    private String userInfoJson;
    private Boolean isAdmin;
}
