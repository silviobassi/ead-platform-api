package com.ead.course.services;

import com.ead.course.models.Course;
import com.ead.course.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    Page<User> findAll(Specification<User> spec, Pageable pageable);

    User create(User user);

    void delete(UUID userId);

    Optional<User> findById(UUID userInstructor);
}
