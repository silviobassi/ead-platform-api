package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record UserCourseDto(@NotNull UUID courseId, UUID userId) implements Serializable {
}