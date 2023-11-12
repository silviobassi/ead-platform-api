package com.ead.eadnotificationhex.core.ports;

import com.ead.eadnotificationhex.core.domain.NotificationDomain;
import com.ead.eadnotificationhex.core.domain.PageInfo;
import com.ead.eadnotificationhex.core.domain.enums.NotificationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationPersistencePort {
    NotificationDomain save(NotificationDomain notificationDomain);
    List<NotificationDomain> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus NotificationStatus, PageInfo pageInfo);
    Optional<NotificationDomain> findByNotificationIdAndAndUserId(UUID notificationId, UUID userId);
}
