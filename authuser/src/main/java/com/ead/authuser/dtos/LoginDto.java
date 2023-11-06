package com.ead.authuser.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank
        String userName,
        @NotBlank
        String password
) {
}
