package com.ead.course.repositories;

import com.ead.course.models.Course;
import com.ead.course.models.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Module, UUID> {
    @Query(value = "select * from module where course_id = :courseId", nativeQuery = true)
    List<Module> findAllModulesIntoCourse(@Param("courseId") UUID courseId);

    @Query(value = "select * from module where module_id = :moduleId and course_id = :courseId", nativeQuery = true)
    Optional<Module> findModuleIntoCourse(UUID moduleId, UUID courseId);
}