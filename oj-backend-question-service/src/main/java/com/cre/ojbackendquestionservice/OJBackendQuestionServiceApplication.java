package com.cre.ojbackendquestionservice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.cre.ojbackendquestionservice.mapper")
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.cre")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cre.ojbackendserviceclient.service"})
@EnableScheduling
public class OJBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OJBackendQuestionServiceApplication.class, args);
    }

}
