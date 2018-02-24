package com.yunjing.config;

import com.yunjing.mommon.base.BaseApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 配置中心启动类
 * @version 1.0.0
 * @author: zhangx
 * @date 2018/2/24 15:34
 * @description
 **/
@SpringBootApplication
public class ConfigApplication extends BaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }
}
