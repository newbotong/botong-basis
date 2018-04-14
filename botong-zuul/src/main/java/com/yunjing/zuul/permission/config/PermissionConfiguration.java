package com.yunjing.zuul.permission.config;

import com.yunjing.zuul.permission.properties.GatewayProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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
}
