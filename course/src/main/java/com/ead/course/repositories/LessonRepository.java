package com.ead.course.repositories;

import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    @Query(value = "select * from lesson where module_id = :lessonId", nativeQuery = true)
    List<Lesson> findAllLessonsIntoModule(@Param("lessonId") UUID lessonId);

    @Query(value = "select * from lesson where lesson_id = :lessonId and module_id = :moduleId", nativeQuery = true)
    Optional<Lesson> findLessonIntoModule(UUID lessonId, UUID moduleId);
}