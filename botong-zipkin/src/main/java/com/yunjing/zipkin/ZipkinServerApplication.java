package com.yunjing.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import zipkin.storage.mysql.MySQLStorage;

import javax.sql.DataSource;


/**
 * @version 1.0.0
 * @author: Gyb
 * @description
 **/
@EnableZipkinStreamServer
@SpringBootApplication
@EnableEurekaClient
public class ZipkinServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZipkinServerApplication.class, args);
    }


    @Bean
    @Primary
    public MySQLStorage mySQLStorage(DataSource dataSource) {
        return MySQLStorage.builder().datasource(dataSource).executor(Runnable::run).build();
    }
}
