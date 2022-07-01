package com.krest.vedio;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auther: krest
 * @Date: 2020/12/5 21:33
 * @Description:
 */
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.krest.vedio.mapper")
@ComponentScan(basePackages = {"com.krest"})
@SpringBootApplication
public class Vedio8003 {
    public static void main(String[] args) {
        SpringApplication.run(Vedio8003.class,args);
    }
}
