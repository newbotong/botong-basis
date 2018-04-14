package com.yunjing.zuul.permission.filter;

import com.yunjing.zuul.permission.constant.Constants;
import com.yunjing.zuul.permission.context.PermissionContext;
import com.yunjing.zuul.permission.validator.PermissionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
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

    /*ignore permission validate  */
    @Override
    public boolean shouldFilter() {
//        PermissionContext context = PermissionContext.getCurrentContext();
//        return !context.ignoredPermissionValidate();
        return false;
    }


    @Override
    public Object run() {
        PermissionContext context = PermissionContext.getCurrentContext();
        permissionValidator.validate();
        context.unset();
        return null;
    }
}
