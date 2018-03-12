package com.yunjing.zuul.dto;

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

    private Long identity;
    private String userInfoJson;
    private Boolean isAdmin;
}
