package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.Course;
import com.ead.course.models.CourseUser;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    final CourseUserRepository courseUserRepository;

    final AuthUserClient authUserClient;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository, AuthUserClient authUserClient){
        this.courseUserRepository = courseUserRepository;
        this.authUserClient = authUserClient;
    }


    @Override
    public boolean existsByCourseAndUserId(Course course, UUID userId) {
        return courseUserRepository.existsByCourseAndUserId(course, userId);
    }

    @Override
    public CourseUser save(CourseUser courseUser) {
        return courseUserRepository.save(courseUser);
    }

    @Override
    @Transactional
    public CourseUser saveAndSendSubscriptionUserInCourse(CourseUser courseUser) {
        courseUser = courseUserRepository.save(courseUser);
        authUserClient.postSubscriptionUserInCourse(courseUser.getCourse().getCourseId(), courseUser.getUserId());
        return courseUser;
    }
}
