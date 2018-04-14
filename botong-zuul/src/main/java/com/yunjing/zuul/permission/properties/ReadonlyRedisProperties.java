package com.yunjing.zuul.permission.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/4/13
 * @description
 **/
@Data
@Validated
@ConfigurationProperties(ReadonlyRedisProperties.PREFIX)
@NoArgsConstructor
public class ReadonlyRedisProperties {

    public static final String PREFIX = "spring.redis-readonly";

    private int database;
    private String host;
    private int port;
    private String password;
    private int timeout;
    private Pool pool;

    @Data
    @Validated
    public static class Pool {
        private int maxActive;
        private int maxWait;
        private int maxIdle;
        private int minIdle;
    }
}
