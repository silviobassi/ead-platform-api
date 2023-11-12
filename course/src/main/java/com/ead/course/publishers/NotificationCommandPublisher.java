package com.ead.course.publishers;

import com.ead.course.dtos.NotificationDomainCommandDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationDomainCommandPublisher {

    final RabbitTemplate rabbitTemplate;
    @Value("${ead.broker.exchange.NotificationDomainCommandExchange}")
    private String NotificationDomainCommandExchange;
    @Value("${ead.broker.key.NotificationDomainCommandKey}")
    private String NotificationDomainCommandKey;

    public NotificationDomainCommandPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishNotificationDomainCommand(NotificationDomainCommandDto NotificationDomainCommandDto){
        rabbitTemplate.convertAndSend(NotificationDomainCommandExchange, NotificationDomainCommandKey, NotificationDomainCommandDto);
    }
}
