package com.ead.authuser.dtos;

import com.ead.authuser.enums.CourseLevel;
import com.ead.authuser.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CourseDto(
        UUID courseId,
        String name,
        String description,
        String imageUrl,
        CourseStatus courseStatus,
        CourseLevel courseLevel,
        UUID userInstructor

) implements Serializable {
}