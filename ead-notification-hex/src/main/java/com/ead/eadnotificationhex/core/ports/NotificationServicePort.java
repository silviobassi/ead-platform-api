package com.ead.eadnotificationhex.core.ports;

import com.ead.eadnotificationhex.core.domain.NotificationDomain;
import com.ead.eadnotificationhex.core.domain.PageInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationServicePort {
    NotificationDomain saveNotification(NotificationDomain notificationDomain);
    List<NotificationDomain> findAllNotificationsByUser(UUID userId, PageInfo pageInfo);
    Optional<NotificationDomain> findByNotificationIdAndUserId(UUID NotificationDomainId, UUID userId);
}
