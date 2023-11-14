package com.ead.course.services;

import com.ead.course.models.Course;
import com.ead.course.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(Course course);

    Course create(Course course);

    Optional<Course> findById(UUID courseId);

    Page<Course> findAllCourses(Specification<Course> spec, Pageable pageable);

    boolean existsByCourseAndUser(UUID courseId, UUID subscriptionUserId);

    void saveSubscriptionUserInCourse(UUID courseId, UUID userId);

    void saveSubscriptionUserInCourseAndSendNotification(Course course, User user);

}
