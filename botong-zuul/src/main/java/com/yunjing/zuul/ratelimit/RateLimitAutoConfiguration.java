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

package com.yunjing.zuul.ratelimit;

import com.google.common.collect.Lists;
import com.netflix.zuul.ZuulFilter;
import com.yunjing.zuul.ratelimit.config.DefaultRateLimitKeyGenerator;
import com.yunjing.zuul.ratelimit.config.RateLimitKeyGenerator;
import com.yunjing.zuul.ratelimit.config.RateLimiter;
import com.yunjing.zuul.ratelimit.config.properties.RateLimitProperties;
import com.yunjing.zuul.ratelimit.config.properties.RateLimitProperties.Policy;
import com.yunjing.zuul.ratelimit.config.repository.DefaultRateLimiterErrorHandler;
import com.yunjing.zuul.ratelimit.config.repository.InMemoryRateLimiter;
import com.yunjing.zuul.ratelimit.config.repository.RateLimiterErrorHandler;
import com.yunjing.zuul.ratelimit.config.repository.RedisRateLimiter;
import com.yunjing.zuul.ratelimit.filters.RateLimitPostFilter;
import com.yunjing.zuul.ratelimit.filters.RateLimitPreFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.util.UrlPathHelper;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static com.yunjing.zuul.ratelimit.config.properties.RateLimitProperties.PREFIX;

/**
 * @author Marcos Barbero
 * @author Liel Chayoun
 */
@Configuration
@EnableConfigurationProperties(RateLimitProperties.class)
@ConditionalOnProperty(prefix = PREFIX, name = "enabled", havingValue = "true")
public class RateLimitAutoConfiguration {

    private final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Bean
    @ConditionalOnMissingBean(RateLimiterErrorHandler.class)
    public RateLimiterErrorHandler rateLimiterErrorHandler() {
        return new DefaultRateLimiterErrorHandler();
    }

    @Bean
    public ZuulFilter rateLimiterPreFilter(final RateLimiter rateLimiter,
                                           final RateLimitProperties rateLimitProperties,
                                           final RouteLocator routeLocator,
                                           final RateLimitKeyGenerator rateLimitKeyGenerator) {
        return new RateLimitPreFilter(rateLimitProperties, routeLocator, urlPathHelper, rateLimiter,
                rateLimitKeyGenerator);
    }

    @Bean
    public ZuulFilter rateLimiterPostFilter(final RateLimiter rateLimiter,
                                            final RateLimitProperties rateLimitProperties,
                                            final RouteLocator routeLocator,
                                            final RateLimitKeyGenerator rateLimitKeyGenerator) {
        return new RateLimitPostFilter(rateLimitProperties, routeLocator, urlPathHelper, rateLimiter,
                rateLimitKeyGenerator);
    }

    @Bean
    @ConditionalOnMissingBean(RateLimitKeyGenerator.class)
    public RateLimitKeyGenerator ratelimitKeyGenerator(final RateLimitProperties properties) {
        return new DefaultRateLimitKeyGenerator(properties);
    }

    @Configuration
    @ConditionalOnClass(RedisTemplate.class)
    @ConditionalOnMissingBean(RateLimiter.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "repository", havingValue = "REDIS")
    public static class RedisConfiguration {

        @Bean("rateLimiterRedisTemplate")
        public StringRedisTemplate redisTemplate(final RedisConnectionFactory connectionFactory) {
            return new StringRedisTemplate(connectionFactory);
        }

        @Bean
        public RateLimiter redisRateLimiter(RateLimiterErrorHandler rateLimiterErrorHandler,
                                            @Qualifier("rateLimiterRedisTemplate") final RedisTemplate redisTemplate) {
            return new RedisRateLimiter(rateLimiterErrorHandler, redisTemplate);
        }
    }

    @Configuration
    @ConditionalOnMissingBean(RateLimiter.class)
    @ConditionalOnProperty(prefix = PREFIX, name = "repository", havingValue = "IN_MEMORY", matchIfMissing = true)
    public static class InMemoryConfiguration {

        @Bean
        public RateLimiter inMemoryRateLimiter(final RateLimiterErrorHandler rateLimiterErrorHandler) {
            return new InMemoryRateLimiter(rateLimiterErrorHandler);
        }
    }

    @Configuration
    @RequiredArgsConstructor
    protected static class RateLimitPropertiesAdjuster {

        private final RateLimitProperties rateLimitProperties;

        @PostConstruct
        public void init() {
            Policy defaultPolicy = rateLimitProperties.getDefaultPolicy();
            if (defaultPolicy != null) {
                ArrayList<Policy> defaultPolicies = Lists.newArrayList(defaultPolicy);
                defaultPolicies.addAll(rateLimitProperties.getDefaultPolicyList());
                rateLimitProperties.setDefaultPolicyList(defaultPolicies);
            }
            rateLimitProperties.getPolicies().forEach((route, policy) ->
                    rateLimitProperties.getPolicyList().compute(route, (key, policies) -> getPolicies(policy, policies)));
        }

        private List<Policy> getPolicies(Policy policy, List<Policy> policies) {
            List<Policy> combinedPolicies = Lists.newArrayList(policy);
            if (policies != null) {
                combinedPolicies.addAll(policies);
            }
            return combinedPolicies;
        }
    }
}
