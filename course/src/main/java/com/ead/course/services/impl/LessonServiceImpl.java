package com.ead.course.services.impl;

import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.services.LessonService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    LessonRepository lessonRepository;

    @Override
    public List<Lesson> findAll() {
        return lessonRepository.findAll();
    }

    @Override
    @Transactional
    public Lesson create(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Override
    public Optional<Lesson> findById(UUID lessonId) {
        return lessonRepository.findById(lessonId);
    }

    @Override
    public void delete(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    @Override
    public List<Lesson> findAllLessonsIntoModule(UUID moduleId) {
        return lessonRepository.findAllLessonsIntoModule(moduleId);
    }

    @Override
    public Optional<Lesson> findLessonIntoModule(UUID lessonId, UUID moduleId) {
        return lessonRepository.findLessonIntoModule(lessonId, moduleId);
    }
}
