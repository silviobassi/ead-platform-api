package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.Course;
import com.ead.course.models.CourseUser;
import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private CourseUserRepository courseUserRepository;

    @Autowired
    private AuthUserClient authUserClient;

    @Override
    @Transactional
    public void delete(Course course) {

        boolean deleteCourseUserInAuthUser = false;

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

        List<CourseUser> courseUserList = courseUserRepository.findAllCourseUserIntoCourse(course.getCourseId());

        if(!courseUserList.isEmpty()){
            courseUserRepository.deleteAll(courseUserList);
            deleteCourseUserInAuthUser = true;
        }

        courseRepository.delete(course);

        if(deleteCourseUserInAuthUser){
            authUserClient.deleteCourseInAuthUser(course.getCourseId());
        }
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


}
