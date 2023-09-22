package com.ead.course.services;

import com.ead.course.models.Course;
import com.ead.course.models.Module;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {
    void delete(Module module);

    Module create(Module module);

    Optional<Module> findById(UUID moduleId);

    List<Module> findAllModulesIntoCourse(UUID courseId);

    Optional<Module> findAllModulesIntoCourse(UUID moduleId, UUID courseId);

}
