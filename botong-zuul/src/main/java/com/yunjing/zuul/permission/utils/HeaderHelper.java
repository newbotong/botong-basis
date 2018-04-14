package com.yunjing.zuul.permission.utils;

import com.netflix.zuul.context.RequestContext;
import com.yunjing.zuul.permission.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
public class HeaderHelper {


    public static String getAuthorization(RequestContext context) {
        return context.getRequest().getHeader(Constants.Header.HEADER_AUTHORIZATION);
    }

    public static String getMemberId(RequestContext context) {
        return context.getRequest().getHeader(Constants.Header.HEADER_MEMBER_ID);
    }
}
