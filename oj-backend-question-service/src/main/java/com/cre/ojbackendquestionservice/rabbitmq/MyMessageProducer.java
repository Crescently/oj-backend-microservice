package com.cre.ojbackendquestionservice.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyMessageProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     */
    public void sendMessage(String exchange, String routingKey, String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

}