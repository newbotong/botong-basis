package com.yunjing.config;

import com.yunjing.mommon.base.BaseApplication;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 健康监控服务启动类
 * @author tandk
 * @date 2018/3/21 17:41
 */
@EnableDiscoveryClient
@SpringBootApplication
@EnableAdminServer
public class AdminMonitorApplication extends BaseApplication {


    public static void main(String[] args) {
        SpringApplication.run(AdminMonitorApplication.class, args);
    }
}
