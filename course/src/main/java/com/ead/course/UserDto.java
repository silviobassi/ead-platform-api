package com.ead.course;

import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.ead.authuser.models.User}
 */
public record UserDto(
        UUID userId,
        String userName,
        String email,
        String fullName,
        UserStatus userStatus,

        UserType userType,
        String phoneNumber,
        String cpf,
        String imageUrl) implements Serializable { }