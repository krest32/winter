package com.krest.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auther: krest
 * @Date: 2020/12/3 16:08
 * @Description:
 */
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.krest.blog.mapper")
@ComponentScan(basePackages = {"com.krest"})
@SpringBootApplication
public class Blog8002 {
    public static void main(String[] args) {
        SpringApplication.run(Blog8002.class,args);
    }
}
