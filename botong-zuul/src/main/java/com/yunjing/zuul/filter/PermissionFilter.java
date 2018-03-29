package com.yunjing.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.dto.JwtUserDto;
import com.yunjing.zuul.dto.ResourceDto;
import com.yunjing.zuul.processor.feign.AdminUserRemoteService;
import com.yunjing.zuul.processor.feign.TokenRemoteService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/12
 * @description
 **/
@Component
public class PermissionFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(PermissionFilter.class);

    @Value("${gate.ignore.uri}")
    private String[] ignorePermissionCheckUrls;

    @Value("${zuul.prefix}")
    private String prefix;

    @Value("${gate.ignore.startWith}")
    private String[] ignoreStartWith;

    @Resource
    @Lazy
    private TokenRemoteService tokenRemoteService;

    @Resource
    @Lazy
    private AdminUserRemoteService adminUserRemoteService;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    private static final String HEADER_AUTHORIZATION = "Authorization";
    private static final String HEADER_BEARER = "Bearer";
    private static final String HEADER_USER_ID = "i";
    private static final String HEADER_USER_INFO = "u";

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String requestUri = request.getRequestURI().substring(prefix.length());

        if (isIgnoreRequestUrl(requestUri)) {
            return null;
        }

        if (isIgnoreRequestUrlStartWith(requestUri)) {
            return null;
        }

        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        final String method = request.getMethod();

        JwtUserDto jwtUserDto = verifyToken(authorization);
        if (null == jwtUserDto) {
            sendForbidden();
            return null;
        }

//        boolean hasPermission = checkPermission(jwtUserDto, requestUri, method);
//        if (!hasPermission) {
//            sendForbidden();
//            return null;
//        }

        ctx.addZuulRequestHeader(HEADER_AUTHORIZATION, authorization);
        ctx.addZuulRequestHeader(HEADER_USER_ID, jwtUserDto.getIdentity().toString());
        ctx.addZuulRequestHeader(HEADER_USER_INFO, jwtUserDto.getUserInfoJson());
        return null;
    }

    /**
     * 忽略链接
     *
     * @param requestUri
     * @return
     */
    private boolean isIgnoreRequestUrl(String requestUri) {
        return Arrays.stream(ignorePermissionCheckUrls).distinct().anyMatch(s -> s.equalsIgnoreCase(requestUri));
    }


    /**
     * URI是否以什么打头
     *
     * @param requestUri
     * @return
     */
    private boolean isIgnoreRequestUrlStartWith(String requestUri) {
        return Arrays.stream(ignoreStartWith).distinct().anyMatch(requestUri::startsWith);
    }

    private JwtUserDto verifyToken(String authorization) {
        if (StringUtils.isNotEmpty(authorization) && authorization.startsWith(HEADER_BEARER)) {
            String token = authorization.split(" ")[1];
            ResponseEntityWrapper<JwtUserDto> response = tokenRemoteService.authentication(token);
            if (response.getStatusCode() == StatusCode.SUCCESS.getStatusCode()) {
                //verify token success
                return response.getData();
            }
        }
        return null;
    }

    private boolean checkPermission(JwtUserDto jwtUserDto, String requestUri, String method) {
        ResponseEntityWrapper<List<ResourceDto>> responseWrapper = adminUserRemoteService.accessResourceListByUser(jwtUserDto.getIdentity());
        boolean anyMatch = false;
        if (responseWrapper.getStatusCode() == StatusCode.SUCCESS.getStatusCode()) {
            List<ResourceDto> accessibleResourceList = responseWrapper.getData();
            if (accessibleResourceList.isEmpty()) return false;
            anyMatch = accessibleResourceList.parallelStream().distinct().anyMatch(resourceDto -> resourceDto.getMethod().equalsIgnoreCase(method)
                    && resourceDto.getUri().equalsIgnoreCase(requestUri));
        }
        return anyMatch;
    }

    private void sendForbidden() {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(HttpStatus.SC_OK);
        ctx.addZuulResponseHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
        ResponseEntityWrapper wrapper = ResponseEntityWrapper.error(StatusCode.FORBIDDEN);
        ctx.setResponseBody(JSON.toJSONString(wrapper));
        ctx.setSendZuulResponse(false);
    }

    private void forwardRequest(String authorization) {
        RequestContext ctx = RequestContext.getCurrentContext();
        if (StringUtils.isNotEmpty(authorization)) {
            ctx.addZuulRequestHeader(HEADER_AUTHORIZATION, authorization);
        }
    }
}
