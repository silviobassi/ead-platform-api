package com.ead.notification.dtos;

import com.ead.notification.enums.NotificationStatus;
import jakarta.validation.constraints.NotNull;

public record NotificationDto(@NotNull NotificationStatus notificationStatus) {
}
