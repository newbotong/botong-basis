package com.yunjing.mircoapps.zuul;

import com.yunjing.mommon.base.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @version 1.0.0
 * @author: Gyb
 * @date 2018/3/15
 * @description
 **/
@SpringBootApplication
@EnableZuulProxy
public class MicroAppsApplication extends BaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroAppsApplication.class, args);
    }
}
