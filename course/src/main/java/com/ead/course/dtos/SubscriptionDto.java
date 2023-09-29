package com.ead.course.dtos;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;
public record SubscriptionDto(
        @NotNull
        UUID userId
)
        implements Serializable {
}