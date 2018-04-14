package com.yunjing.zuul.permission.filter;

import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.global.exception.BaseRuntimeException;
import com.yunjing.zuul.permission.constant.Constants;
import com.yunjing.zuul.permission.context.PermissionContext;
import com.yunjing.zuul.permission.properties.GatewayProperties;
import com.yunjing.zuul.permission.utils.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
@Component
public class IgnorePermissionValidateFilter extends AbstractZuulFilter {

    @Autowired
    private GatewayProperties gatewayProperties;

    @Value("${zuul.prefix}")
    private String prefix;

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constants.FilterOrder.IGNORE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        final String requestUri = request.getRequestURI().substring(prefix.length());
        logger.info("request url = {}", requestUri);
        String[] ignoreStartWithArray = gatewayProperties.getIgnore().getStartWith();
        boolean isIgnored = isIgnoreRequestUrlStartWith(ignoreStartWithArray, requestUri);

        if (!isIgnored) {
            String[] ignoreUrls = gatewayProperties.getIgnore().getUri();
            isIgnored = isIgnoreRequestUrl(ignoreUrls, requestUri);
        }

        //上下文保存，是否是忽略链接的标志位
        PermissionContext context = PermissionContext.getCurrentContext();
        context.setIgnoredPermissionValidate(isIgnored);
        //构建Request Header，转发请求
        new ResponseBuilder(RequestContext.getCurrentContext())
                .buildRequestHeaderWithApplicationJson()
                .buildRequestHeaderWithToken()
                .buildRequestHeaderWithMember()
                .build();
//        try {
//
//        } catch (BaseRuntimeException e) {
//            logger.error("过滤器{IgnorePermissionValidateFilter}发生错误", e.getMessage(), e);
//            ZuulException exception = new ZuulException(e, e.getCode(), e.getMessage());
//            throw new ZuulRuntimeException(exception);
//        } catch (Exception e) {
//            logger.error("过滤器{IgnorePermissionValidateFilter}发生错误", e.getMessage(), e);
//            ZuulException exception = new ZuulException(e, StatusCode.ERROR.getStatusCode(), e.getMessage());
//            throw new ZuulRuntimeException(exception);
//        }
        return null;
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isIgnoreRequestUrlStartWith(String[] ignoreArray, String requestUri) {
        return Arrays.stream(ignoreArray).distinct().anyMatch(requestUri::startsWith);
    }

    /**
     * 忽略链接
     *
     * @param requestUri
     * @return
     */
    private boolean isIgnoreRequestUrl(String[] ignoreArray, String requestUri) {
        return Arrays.stream(ignoreArray).distinct().anyMatch(s -> s.equalsIgnoreCase(requestUri));
    }

}
