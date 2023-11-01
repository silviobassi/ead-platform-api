package com.ead.notification.dtos;

import java.util.UUID;

public record NotificationCommandDto(
        String title,
        String message,
        UUID userId
) {
}
