package com.ead.course.publishers;

import com.ead.course.dtos.NotificationCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationCommandPublisher {

    final RabbitTemplate rabbitTemplate;
    @Value("${ead.broker.exchange.notificationCommandExchange}")
    private String NotificationDomainCommandExchange;
    @Value("${ead.broker.key.notificationCommandKey}")
    private String NotificationDomainCommandKey;

    public NotificationCommandPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishNotificationDomainCommand(NotificationCommandDto NotificationDomainCommandDto){
        rabbitTemplate.convertAndSend(NotificationDomainCommandExchange, NotificationDomainCommandKey, NotificationDomainCommandDto);
    }
}
