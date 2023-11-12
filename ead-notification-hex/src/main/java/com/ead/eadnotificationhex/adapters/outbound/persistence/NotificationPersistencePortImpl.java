package com.ead.eadnotificationhex.adapters.outbound.persistence;

import com.ead.eadnotificationhex.core.domain.NotificationDomain;
import com.ead.eadnotificationhex.core.domain.PageInfo;
import com.ead.eadnotificationhex.core.domain.enums.NotificationStatus;
import com.ead.eadnotificationhex.core.ports.NotificationPersistencePort;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class NotificationPersistencePortImpl implements NotificationPersistencePort {

    private final NotificationJpaRepository notificationJpaRepository;

    private final ModelMapper modelMapper;

    public NotificationPersistencePortImpl(NotificationJpaRepository notificationJpaRepository, ModelMapper modelMapper) {
        this.notificationJpaRepository = notificationJpaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public NotificationDomain save(NotificationDomain notificationDomain) {
        return null;
    }

    @Override
    public List<NotificationDomain> findAllByUserIdAndNotificationStatus(UUID userId, NotificationStatus NotificationStatus, PageInfo pageInfo) {
        return null;
    }

    @Override
    public Optional<NotificationDomain> findByNotificationIdAndAndUserId(UUID notificationId, UUID userId) {
        return Optional.empty();
    }
}
