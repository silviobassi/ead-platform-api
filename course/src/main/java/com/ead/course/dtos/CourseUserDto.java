package com.ead.course.dtos;

import java.io.Serializable;
import java.util.UUID;
public record CourseUserDto(
        UUID courseId,
        UUID userId
) implements Serializable {
}