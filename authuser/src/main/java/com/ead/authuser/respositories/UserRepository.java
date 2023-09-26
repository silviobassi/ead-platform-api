package com.ead.authuser.respositories;

import com.ead.authuser.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
}