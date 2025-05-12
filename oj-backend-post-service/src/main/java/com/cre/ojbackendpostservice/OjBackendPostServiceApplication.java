package com.cre.ojbackendpostservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan(basePackages = "com.cre.ojbackendpostservice.mapper")
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.cre")
@EnableFeignClients(basePackages = {"com.cre.ojbackendserviceclient.service"})
@EnableDiscoveryClient
public class OjBackendPostServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjBackendPostServiceApplication.class, args);
    }

}
