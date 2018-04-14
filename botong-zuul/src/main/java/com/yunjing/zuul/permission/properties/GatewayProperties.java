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
@ConfigurationProperties(GatewayProperties.PREFIX)
@NoArgsConstructor
public class GatewayProperties {

    public final static String PREFIX = "gate";

    private Ignore ignore;

    @Data
    @Validated
    public static class Ignore {
        private String[] startWith;

        private String[] uri;
    }
}
