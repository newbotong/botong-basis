package com.yunjing.eureka;

import com.yunjing.mommon.base.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.RestController;


/**
 * 祖册中心启动类
 * @version 1.0.0
 * @author: zhangx
 * @date 2018/2/24 15:31
 * @description
 **/
@SpringBootApplication
@EnableEurekaServer
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class EurekaApplication extends BaseApplication{

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
