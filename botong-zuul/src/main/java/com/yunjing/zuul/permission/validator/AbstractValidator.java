package com.yunjing.zuul.permission.validator;

import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.web.util.UrlPathHelper;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/16
 * @description
 **/
public abstract class AbstractValidator implements Validator {

    private final RouteLocator routeLocator;
    private final UrlPathHelper urlPathHelper;

    public AbstractValidator(RouteLocator routeLocator, UrlPathHelper urlPathHelper) {
        this.routeLocator = routeLocator;
        this.urlPathHelper = urlPathHelper;
    }

    Route route() {
        String requestURI = urlPathHelper.getPathWithinApplication(RequestContext.getCurrentContext().getRequest());
        return routeLocator.getMatchingRoute(requestURI);
    }
}
