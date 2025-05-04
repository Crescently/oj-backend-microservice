package com.cre.ojbackenduserservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan(basePackages = "com.cre.ojbackenduserservice.mapper")
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.cre")
@EnableDiscoveryClient
public class OJBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OJBackendUserServiceApplication.class, args);
    }

}
