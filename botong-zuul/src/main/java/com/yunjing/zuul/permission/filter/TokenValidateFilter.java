package com.yunjing.zuul.permission.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import com.yunjing.zuul.permission.constant.Constants;
import com.yunjing.zuul.permission.context.PermissionContext;
import com.yunjing.zuul.permission.dto.JwtUserDto;
import com.yunjing.zuul.permission.utils.ResponseBuilder;
import com.yunjing.zuul.permission.validator.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
@Component
public class TokenValidateFilter extends AbstractZuulFilter {

    @Autowired
    private TokenValidator tokenValidator;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constants.FilterOrder.TOKEN_FILER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        PermissionContext context = PermissionContext.getCurrentContext();
        return !context.ignoredPermissionValidate();
    }

    @Override
    public Object run() {
        tokenValidator.validate();
        JwtUserDto jwtUserDto = PermissionContext.getCurrentContext().getJwtUser();
        logger.info("validate Token , request user info : {}", JSON.toJSONString(jwtUserDto));
        new ResponseBuilder(RequestContext.getCurrentContext())
                .buildRequestHeaderWithToken()
                .buildRequestHeaderWithMember()
                .buildRequestHeaderWithApplicationJson()
                .buildRequestHeaderWithUserInfo(jwtUserDto)
                .build();
        PermissionContext.getCurrentContext().setCurrentUser(jwtUserDto);
        return null;
    }
}
