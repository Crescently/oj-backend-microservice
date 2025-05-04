package com.cre.ojbackendjudgeservice;

import com.cre.ojbackendjudgeservice.rabbitmq.InitRabbitMq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@Slf4j
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.cre")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cre.ojbackendserviceclient.service"})
public class OJBackendJudgeServiceApplication {
    public static void main(String[] args) {
        InitRabbitMq.doInit();
        SpringApplication.run(OJBackendJudgeServiceApplication.class, args);
    }
}
