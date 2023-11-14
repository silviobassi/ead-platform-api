package com.ead.course.services.impl;

import com.ead.course.dtos.NotificationCommandDto;
import com.ead.course.models.Course;
import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import com.ead.course.models.User;
import com.ead.course.publishers.NotificationCommandPublisher;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
public class CourseServiceImpl implements CourseService {

    final CourseRepository courseRepository;
    final ModuleRepository moduleRepository;
    final LessonRepository lessonRepository;
    final NotificationCommandPublisher notificationCommandPublisher;


    public CourseServiceImpl(CourseRepository courseRepository, ModuleRepository moduleRepository,
                             LessonRepository lessonRepository, NotificationCommandPublisher notificationCommandPublisher) {
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.lessonRepository = lessonRepository;
        this.notificationCommandPublisher = notificationCommandPublisher;
    }

    @Override
    @Transactional
    public void delete(Course course) {
        List<Module> moduleList = moduleRepository.findAllModulesIntoCourse(course.getCourseId());
        if (!moduleList.isEmpty()) {
            for (Module model : moduleList) {
                List<Lesson> lessonList = lessonRepository.findAllLessonsIntoModule(model.getModuleId());
                if (!lessonList.isEmpty()) {
                    lessonRepository.deleteAll(lessonList);
                }
            }
            moduleRepository.deleteAll(moduleList);
        }
        courseRepository.deleteCourseUserByCourse(course.getCourseId());
        courseRepository.delete(course);
    }

    @Transactional
    @Override
    public Course create(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> findById(UUID courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Page<Course> findAllCourses(Specification<Course> spec, Pageable pageable) {
        return courseRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID subscriptionUserId) {
        return courseRepository.existsByCourseAndUser(courseId, subscriptionUserId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
        courseRepository.saveCourseUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourseAndSendNotification(Course course, User user) {
        courseRepository.saveCourseUser(course.getCourseId(), user.getUserId());
        try {
            NotificationCommandDto NotificationDomainCommandDto = new NotificationCommandDto(
                    "Bem-vindo(a) ao Curso: " + course.getName(),
                    user.getFullName() + " a sua inscrição foi realizada com sucesso!",
                    user.getUserId()
            );
            notificationCommandPublisher.publishNotificationDomainCommand(NotificationDomainCommandDto);
        } catch (Exception e) {
            log.warn("Error sending NotificationDomain!");
        }

    }

}
