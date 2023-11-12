package com.ead.eadnotificationhex.adapters.outbound.persistence;

import com.ead.eadnotificationhex.adapters.outbound.entities.NotificationEntity;
import com.ead.eadnotificationhex.core.domain.enums.NotificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, UUID> {

    Page<NotificationEntity> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus notificationStatus, Pageable pageable);

    Optional<NotificationEntity> findByNotificationIdAndAndUserId (UUID notificationId, UUID userId);

}