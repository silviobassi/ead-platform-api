package com.ead.notification.consumers;

import com.ead.notification.dtos.NotificationCommandDto;
import com.ead.notification.enums.NotificationStatus;
import com.ead.notification.models.Notification;
import com.ead.notification.services.NotificationService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class NotificationConsumer {

    final NotificationService notificationService;

    public NotificationConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${ead.broker.queue.notificationCommandQueue.name}", durable = "true"),
            exchange = @Exchange(
                    value = "${ead.broker.exchange.notificationCommandExchange}", type = ExchangeTypes.TOPIC
            ),
            key = "${ead.broker.key.notificationCommandKey}"
    ))
    public void listen(@Payload NotificationCommandDto notificationCommandDto) {
        var notification = new Notification();
        BeanUtils.copyProperties(notificationCommandDto, notification);
        notification.setCreationDate(OffsetDateTime.now());
        notification.setNotificationStatus(NotificationStatus.CREATED);
        notificationService.saveNotification(notification);
    }
}
