package com.ead.course.dtos;

import java.util.UUID;

public record NotificationDomainCommandDto(
        String title,
        String message,
        UUID userId
) {
}
