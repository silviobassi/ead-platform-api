package com.ead.course.repositories;

import com.ead.course.models.User;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationResolver;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

}