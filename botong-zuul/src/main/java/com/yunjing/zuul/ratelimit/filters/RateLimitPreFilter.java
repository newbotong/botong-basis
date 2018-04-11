/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yunjing.zuul.ratelimit.filters;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yunjing.mommon.wrapper.ResponseEntityWrapper;
import com.yunjing.zuul.ratelimit.config.Rate;
import com.yunjing.zuul.ratelimit.config.RateLimitKeyGenerator;
import com.yunjing.zuul.ratelimit.config.RateLimiter;
import com.yunjing.zuul.ratelimit.config.properties.RateLimitProperties;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.yunjing.zuul.ratelimit.support.RateLimitConstants.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.FORM_BODY_WRAPPER_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * @author Marcos Barbero
 * @author Michal Šváb
 * @author Liel Chayoun
 */
public class RateLimitPreFilter extends AbstractRateLimitFilter {

    private final RateLimiter rateLimiter;
    private final RateLimitKeyGenerator rateLimitKeyGenerator;

    public RateLimitPreFilter(final RateLimitProperties properties, final RouteLocator routeLocator,
                              final UrlPathHelper urlPathHelper, final RateLimiter rateLimiter,
                              final RateLimitKeyGenerator rateLimitKeyGenerator) {
        super(properties, routeLocator, urlPathHelper);
        this.rateLimiter = rateLimiter;
        this.rateLimitKeyGenerator = rateLimitKeyGenerator;
    }

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FORM_BODY_WRAPPER_FILTER_ORDER;
    }

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        final HttpServletResponse response = ctx.getResponse();
        final HttpServletRequest request = ctx.getRequest();
        final Route route = route();

        policy(route).forEach(policy -> {
            final String key = rateLimitKeyGenerator.key(request, route, policy);
            final Rate rate = rateLimiter.consume(policy, key, null);
            final String httpHeaderKey = key.replaceAll("[^A-Za-z0-9-.]", "_").replaceAll("__", "_");

            final Long limit = policy.getLimit();
            final Long remaining = rate.getRemaining();
            if (limit != null) {
                response.setHeader(HEADER_LIMIT + httpHeaderKey, String.valueOf(limit));
                response.setHeader(HEADER_REMAINING + httpHeaderKey, String.valueOf(Math.max(remaining, 0)));
            }

            final Long quota = policy.getQuota();
            final Long remainingQuota = rate.getRemainingQuota();
            if (quota != null) {
                RequestContextHolder.getRequestAttributes()
                        .setAttribute(REQUEST_START_TIME, System.currentTimeMillis(), SCOPE_REQUEST);
                response.setHeader(HEADER_QUOTA + httpHeaderKey, String.valueOf(quota));
                response.setHeader(HEADER_REMAINING_QUOTA + httpHeaderKey,
                        String.valueOf(MILLISECONDS.toSeconds(Math.max(remainingQuota, 0))));
            }

            response.setHeader(HEADER_RESET + httpHeaderKey, String.valueOf(rate.getReset()));

            boolean flag = (limit != null && remaining < 0) || (quota != null && remainingQuota < 0);
            if (flag) {
                HttpStatus tooManyRequests = HttpStatus.TOO_MANY_REQUESTS;
                sendErrorResponse(tooManyRequests.value(), "请求次数超过限制");
            }
        });

        return null;
    }

    private void sendErrorResponse(int statusCode, String message) {
        RequestContext ctx = RequestContext.getCurrentContext();
        ctx.setResponseStatusCode(org.apache.http.HttpStatus.SC_OK);
        ctx.addZuulResponseHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString());
        ResponseEntityWrapper wrapper = ResponseEntityWrapper.error(statusCode, message);
        ctx.setResponseBody(JSON.toJSONString(wrapper));
        ctx.setSendZuulResponse(false);
    }

}
