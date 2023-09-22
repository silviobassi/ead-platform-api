package com.ead.course.services;

import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonService {

     List<Lesson> findAll();

     Lesson create(Lesson lesson);

     Optional<Lesson> findById(UUID moduleId);

     void delete(Lesson lesson);

     List<Lesson> findAllLessonsIntoModule(UUID moduleId);

     Optional<Lesson> findLessonIntoModule(UUID lessonId, UUID moduleId);
}
