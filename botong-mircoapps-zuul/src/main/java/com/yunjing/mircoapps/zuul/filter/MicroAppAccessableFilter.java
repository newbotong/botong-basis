package com.yunjing.mircoapps.zuul.filter;

import com.netflix.zuul.ZuulFilter;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/15
 * @description
 **/
public class MicroAppAccessableFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        return null;
    }
}
