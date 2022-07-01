package com.krest.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auther: krest
 * @Date: 2020/12/6 22:41
 * @Description:
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.krest.member.mapper")
@ComponentScan(basePackages = {"com.krest"})
public class Member8004 {
    public static void main(String[] args) {
        SpringApplication.run(Member8004.class,args);
    }
}

