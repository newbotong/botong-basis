package com.yunjing.eureka;

import com.yunjing.mommon.base.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 祖册中心启动类
 * @version 1.0.0
 * @author: zhangx
 * @date 2018/2/24 15:31
 * @description
 **/
@SpringBootApplication
public class EurekaApplication extends BaseApplication{

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
