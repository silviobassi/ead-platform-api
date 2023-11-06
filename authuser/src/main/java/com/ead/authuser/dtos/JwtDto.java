package com.ead.authuser.dtos;

import lombok.NonNull;

public record JwtDto(@NonNull String token) {
    public final static String TYPE = "Bearer";
}
