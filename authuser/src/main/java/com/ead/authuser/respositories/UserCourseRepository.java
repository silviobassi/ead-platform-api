package com.ead.authuser.respositories;

import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourse, UUID> {

    boolean existsByUserAndCourseId(User user, UUID courseId);

    @Query(value = "select * from user_course where user_id = :userId", nativeQuery = true)
    List<UserCourse> findAllUserCourseIntoUser(@Param("userId") UUID userId);
}