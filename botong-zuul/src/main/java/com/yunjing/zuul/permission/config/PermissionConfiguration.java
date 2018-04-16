package com.yunjing.zuul.permission.config;

import com.netflix.zuul.ZuulFilter;
import com.yunjing.zuul.permission.filter.PermissionFilter;
import com.yunjing.zuul.permission.properties.GatewayProperties;
import feign.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.UrlPathHelper;

import static com.yunjing.zuul.permission.properties.GatewayProperties.PREFIX;


/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
@Configuration
@EnableConfigurationProperties(GatewayProperties.class)
@ConditionalOnProperty(prefix = PREFIX, name = "enabled", havingValue = "true")
public class PermissionConfiguration {

    @Bean
    public UrlPathHelper urlPathHelper() {
        return new UrlPathHelper();
    }
}
