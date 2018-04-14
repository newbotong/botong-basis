package com.yunjing.zuul.permission.filter;

import com.netflix.zuul.exception.ZuulException;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.global.exception.BaseRuntimeException;
import com.yunjing.zuul.permission.constant.Constants;
import com.yunjing.zuul.permission.context.PermissionContext;
import com.yunjing.zuul.permission.validator.PermissionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
@Component
public class PermissionFilter extends AbstractZuulFilter {

    @Autowired
    private PermissionValidator permissionValidator;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constants.FilterOrder.PERMISSION_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        PermissionContext context = PermissionContext.getCurrentContext();
        return !context.ignoredPermissionValidate();
    }


    @Override
    public Object run() {
        PermissionContext context = PermissionContext.getCurrentContext();
        permissionValidator.validate();
        context.unset();
        return null;
    }
}
