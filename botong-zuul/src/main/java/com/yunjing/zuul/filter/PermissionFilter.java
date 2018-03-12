package com.yunjing.zuul.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.yunjing.mommon.constant.StatusCode;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.mommon.wrapper.RpcResponseWrapper;
import com.yunjing.zuul.dto.JwtUserDto;
import com.yunjing.zuul.dto.ResourceDto;
import com.yunjing.zuul.processor.feign.AdminUserRemoteService;
import com.yunjing.zuul.processor.feign.TokenRemoteService;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
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

    @Value("${gate.ignore.uri}")
    private String[] ignorePermissionCheckUrls;

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

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        final String requestUri = request.getRequestURI();
        String authorization = request.getHeader(HEADER_AUTHORIZATION);

        if (isIgnoreRequestUrl(requestUri)) {
            forwardRequest(authorization);
            return null;
        }

        JwtUserDto jwtUserDto = verifyToken(authorization);
        if (null == jwtUserDto) {
            sendForbidden();
            return null;
        }
        boolean hasPermission = checkPermission(jwtUserDto, requestUri);
        if (!hasPermission) {
            sendForbidden();
            return null;
        }

        ctx.addZuulRequestHeader(HEADER_AUTHORIZATION, authorization);
        return null;
    }

    private boolean isIgnoreRequestUrl(String requestUri) {
        return Arrays.stream(ignorePermissionCheckUrls).distinct().anyMatch(s -> s.equalsIgnoreCase(requestUri));
    }

    private JwtUserDto verifyToken(String authorization) {
        if (StringUtils.isNotEmpty(authorization) && authorization.startsWith(HEADER_BEARER)) {
            String token = authorization.split(" ")[1];
            RpcResponseWrapper response = tokenRemoteService.authentication(token);
            if (response.getStatusCode() == StatusCode.SUCCESS.getStatusCode()) {
                //verify token success
                return JSON.parseObject(response.getData(), JwtUserDto.class);
            }
        }
        return null;
    }

    private boolean checkPermission(JwtUserDto jwtUserDto, String requestUri) {
        RpcResponseWrapper responseWrapper = adminUserRemoteService.accessResourceListByUser(jwtUserDto.getIdentity());
        boolean anyMatch = false;
        if (responseWrapper.getStatusCode() == StatusCode.SUCCESS.getStatusCode()) {
            List<ResourceDto> accessibleResourceList = JSON.parseArray(responseWrapper.getData(), ResourceDto.class);
            if (accessibleResourceList.isEmpty()) return false;
            anyMatch = accessibleResourceList.parallelStream().distinct().anyMatch(resourceDto -> resourceDto.getUri().equalsIgnoreCase(requestUri));
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
        ctx.setSendZuulResponse(true);
    }
}