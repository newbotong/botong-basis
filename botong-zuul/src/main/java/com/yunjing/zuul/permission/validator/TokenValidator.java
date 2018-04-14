package com.yunjing.zuul.permission.validator;

import com.netflix.zuul.context.RequestContext;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.global.exception.BaseRuntimeException;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.permission.constant.Constants;
import com.yunjing.zuul.permission.context.PermissionContext;
import com.yunjing.zuul.permission.dto.JwtUserDto;
import com.yunjing.zuul.permission.processor.feign.TokenRemoteService;
import com.yunjing.zuul.permission.utils.HeaderHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description Token验证器
 **/
@Component
public class TokenValidator implements Validator {

    @Resource
    @Lazy
    private TokenRemoteService tokenRemoteService;

    /**
     * 获取token并做非空校验
     *
     * @return
     */
    private String getToken() {
        String authorization = HeaderHelper.getAuthorization(RequestContext.getCurrentContext());
        if (StringUtils.isEmpty(authorization)) {
            throw new BaseRuntimeException(StatusCode.TOKEN_IS_EMPTY);
        }

        if (!authorization.startsWith(Constants.Header.HEADER_BEARER)) {
            throw new BaseRuntimeException(StatusCode.TOKEN_FORMAT_ERR);
        }

        String token = authorization.split(" ")[1];
        if (StringUtils.isEmpty(token)) {
            throw new BaseRuntimeException(StatusCode.TOKEN_IS_EMPTY);
        }
        return token;
    }

    /**
     * 校验token
     *
     * @return token中加密的用户信息
     */
    @Override
    public void validate() {
        String token = getToken();
        ResponseEntityWrapper<JwtUserDto> response = tokenRemoteService.authentication(token);
        if (response.getStatusCode() == StatusCode.SUCCESS.getStatusCode()) {
            JwtUserDto jwtUserDto = response.getData();
            PermissionContext.getCurrentContext().setCurrentUser(jwtUserDto);
        } else {
            throw new BaseRuntimeException(response.getStatusCode(), response.getStatusMessage());
        }
    }
}
