package com.ead.course.publishers;

import com.ead.course.dtos.NotificationCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {

    final RabbitTemplate rabbitTemplate;
    @Value("${ead.broker.exchange.notificationCommandExchange}")
    private String notificationCommandExchange;
    @Value("${ead.broker.key.notificationCommandKey}")
    private String notificationCommandKey;

    public NotificationCommandPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishNotificationCommand(NotificationCommandDto notificationCommandDto){
        rabbitTemplate.convertAndSend(notificationCommandExchange, notificationCommandKey, notificationCommandDto);
    }
}
