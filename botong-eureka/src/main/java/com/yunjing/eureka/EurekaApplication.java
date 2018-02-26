package com.yunjing.eureka;

import com.yunjing.mommon.base.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 注册中心中心启动类
 * @version 1.0.0
 * @author: zhangx
 * @date 2018/2/24 15:31
 * @description
 **/
@RestController
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication extends BaseApplication{

    @GetMapping("/info")
    public String info(){
        return "info";
    }

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
