package com.yunjing.mircoapps.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yunjing.mircoapps.zuul.utils.ResponseBuilder;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.global.exception.BaseRuntimeException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description 错误过滤器
 **/
@Component
public class ErrorFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.ERROR_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.SEND_ERROR_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        Throwable throwable = context.getThrowable();
        Throwable cause = throwable.getCause();
        if (cause instanceof BaseRuntimeException) {
            new ResponseBuilder(context).buildErrorResponse(((BaseRuntimeException) cause).getCode(), cause.getMessage()).build();
        } else {
            if (cause instanceof ZuulException) {
                new ResponseBuilder(context).buildErrorResponse(((ZuulException) cause).nStatusCode, cause.getMessage()).build();
            } else {
                new ResponseBuilder(context).buildErrorResponse(StatusCode.ERROR.getStatusCode(), cause.getMessage()).build();
            }
        }

        return null;
    }
}

