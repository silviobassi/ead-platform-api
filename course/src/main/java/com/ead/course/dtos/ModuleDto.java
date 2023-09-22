package com.ead.course.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

public record ModuleDto(
        @NotBlank
        String title,
        @NotBlank
        String description,
        @NotNull
        UUID courseId
) implements Serializable { }