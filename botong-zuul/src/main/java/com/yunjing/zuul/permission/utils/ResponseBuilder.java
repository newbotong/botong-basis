package com.yunjing.zuul.permission.utils;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.global.exception.BaseRuntimeException;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.permission.constant.Constants;
import com.yunjing.zuul.permission.dto.JwtUserDto;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
public class ResponseBuilder {

    private RequestContext context;


    public ResponseBuilder(RequestContext context) {
        this.context = context;
    }

    public ResponseBuilder buildRequestHeaderWithToken() {
        String authorization = HeaderHelper.getAuthorization(context);
        context.addZuulRequestHeader(Constants.Header.HEADER_AUTHORIZATION, authorization);
        return this;
    }

    public ResponseBuilder buildRequestHeaderWithUserInfo(JwtUserDto jwtUser) {
        if (Objects.isNull(jwtUser)) {
            throw new BaseRuntimeException(StatusCode.TOKEN_IS_EMPTY);
        }

        context.addZuulRequestHeader(Constants.Header.HEADER_USER_ID, jwtUser.getIdentity());
        context.addZuulRequestHeader(Constants.Header.HEADER_USER_INFO, jwtUser.getUserInfoJson());
        return this;
    }

    public ResponseBuilder build() {
        return this;
    }


    public ResponseBuilder buildErrorResponse(int statusCode, String message) {
        HttpServletResponse response = context.getResponse();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
        response.setStatus(HttpStatus.SC_OK);
        try (PrintWriter writer = response.getWriter()) {
            ResponseEntityWrapper wrapper = ResponseEntityWrapper.error(statusCode, message);
            writer.print(JSON.toJSONString(wrapper));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ResponseBuilder buildRequestHeaderWithApplicationJson() {
        context.addZuulResponseHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
        return this;
    }

    public ResponseBuilder buildRequestHeaderWithMember() {
        context.addZuulRequestHeader(Constants.Header.HEADER_MEMBER_ID, HeaderHelper.getMemberId(context));
        return this;
    }
}
