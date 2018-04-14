package com.yunjing.zuul.permission.filter;

import com.netflix.zuul.ZuulFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
public abstract class AbstractZuulFilter extends ZuulFilter {

    protected Logger logger = LoggerFactory.getLogger(AbstractZuulFilter.class);
}
