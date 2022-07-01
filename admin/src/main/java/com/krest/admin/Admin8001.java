package com.krest.admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auther: krest
 * @Date: 2020/12/3 16:16
 * @Description:
 */

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.krest.admin.mapper")
@ComponentScan(basePackages = {"com.krest"})
public class Admin8001 {
    public static void main(String[] args) {
        SpringApplication.run(Admin8001.class,args);
    }
}
