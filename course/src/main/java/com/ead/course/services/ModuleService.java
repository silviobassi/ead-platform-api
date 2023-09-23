package com.ead.course.services;

import com.ead.course.models.Module;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.BitSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ModuleService {
    void delete(Module module);

    Module create(Module module);

    Optional<Module> findById(UUID moduleId);

    List<Module> findAllModuleIntoCourse(UUID courseId);

    Optional<Module> findModuleIntoCourse(UUID courseId, UUID moduleId);

    Page<Module> findAllByCourse(Specification<Module> spec, Pageable pageable);
}
