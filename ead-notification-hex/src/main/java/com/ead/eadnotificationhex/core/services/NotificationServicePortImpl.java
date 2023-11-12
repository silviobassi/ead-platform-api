package com.ead.eadnotificationhex.core.services;

import com.ead.eadnotificationhex.core.domain.NotificationDomain;
import com.ead.eadnotificationhex.core.domain.PageInfo;
import com.ead.eadnotificationhex.core.domain.enums.NotificationStatus;
import com.ead.eadnotificationhex.core.ports.NotificationPersistencePort;
import com.ead.eadnotificationhex.core.ports.NotificationServicePort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class NotificationServicePortImpl implements NotificationServicePort {

    final NotificationPersistencePort notificationPersistencePort;

    public NotificationServicePortImpl(NotificationPersistencePort notificationPersistencePort) {
        this.notificationPersistencePort = notificationPersistencePort;
    }

    public NotificationDomain saveNotification(NotificationDomain notificationDomain) {
        return notificationPersistencePort.save(notificationDomain);
    }

    public List<NotificationDomain> findAllNotificationsByUser(UUID userId, PageInfo pageInfo) {
        return notificationPersistencePort.findAllByUserIdAndNotificationStatus(userId, NotificationStatus.CREATED, pageInfo);
    }

    public Optional<NotificationDomain> findByNotificationIdAndUserId(UUID notificationId, UUID userId) {
        return notificationPersistencePort.findByNotificationIdAndAndUserId(notificationId, userId);
    }
}