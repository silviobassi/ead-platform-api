package com.authuser.authuser.services;

import com.authuser.authuser.models.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findById(UUID userId);

    void delete(UserModel user);

    UserModel create(UserModel userModel);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);
}
