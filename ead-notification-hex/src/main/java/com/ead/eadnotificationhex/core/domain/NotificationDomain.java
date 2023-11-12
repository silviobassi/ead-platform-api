package com.ead.eadnotificationhex.core.domain;

import java.time.OffsetDateTime;
import com.ead.eadnotificationhex.core.domain.enums.NotificationStatus;

import java.util.Objects;
import java.util.UUID;

public class NotificationDomain {

    private UUID notificationId;
    private UUID userId;
    private String title;
    private String message;
    private OffsetDateTime creationDate;
    private NotificationStatus NotificationStatus;

    public UUID getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(UUID notificationId) {
        this.notificationId = notificationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public com.ead.eadnotificationhex.core.domain.enums.NotificationStatus getNotificationStatus() {
        return NotificationStatus;
    }

    public void setNotificationStatus(com.ead.eadnotificationhex.core.domain.enums.NotificationStatus notificationStatus) {
        NotificationStatus = notificationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationDomain that = (NotificationDomain) o;

        if (!Objects.equals(notificationId, that.notificationId))
            return false;
        if (!Objects.equals(userId, that.userId)) return false;
        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(message, that.message)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        return NotificationStatus == that.NotificationStatus;
    }

    @Override
    public int hashCode() {
        int result = notificationId != null ? notificationId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (NotificationStatus != null ? NotificationStatus.hashCode() : 0);
        return result;
    }
}
