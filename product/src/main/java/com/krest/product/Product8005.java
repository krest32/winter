package com.krest.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Auther: krest
 * @Date: 2020/12/4 16:59
 * @Description:
 */
@EnableRabbit
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.krest.product.mapper")
@ComponentScan(basePackages = {"com.krest"})
public class Product8005 {
    public static void main(String[] args) {
        SpringApplication.run(Product8005.class,args);
    }
}
