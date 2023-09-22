package com.ead.course.services;

import com.ead.course.models.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(Course course);

    Course create(Course course);

    Optional<Course> findById(UUID courseId);

    List<Course> findAll();
}
