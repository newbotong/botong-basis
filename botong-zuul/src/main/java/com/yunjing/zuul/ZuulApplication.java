package com.yunjing.zuul;

import com.yunjing.mommon.base.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 路由启动类
 * @version 1.0.0
 * @author: zhangx
 * @date 2018/2/24 15:26
 * @description
 **/
@RestController
@EnableEurekaClient
@EnableZuulProxy
@SpringBootApplication
public class ZuulApplication extends BaseApplication {

    @GetMapping("/hello")
    public String hello(){
        return "hello word";
    }

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}
