package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.Course;
import com.ead.course.models.Module;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;


    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<?> getAllModules(@PathVariable(name = "courseId") UUID courseId) {
        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllModulesIntoCourse(courseId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/modules")
    public ResponseEntity<?> create(@RequestBody @Valid ModuleDto moduleDTO) {
        Optional<Course> courseCurrent = courseService.findById(moduleDTO.courseId());
        if (courseCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        var module = new Module();
        BeanUtils.copyProperties(moduleDTO, module);
        module.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        module.setCourse(courseCurrent.get());
        module.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.create(module));
    }

    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> delete(@PathVariable(value = "moduleId") UUID moduleId,
                                    @PathVariable(value = "moduleId") UUID courseId) {
        Optional<Module> moduleIntoCourse = moduleService.findAllModulesIntoCourse(moduleId, courseId);

        if (moduleIntoCourse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course");
        }
        moduleService.delete(moduleIntoCourse.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> update(@PathVariable(value = "moduleId") UUID moduleId,
                                    @PathVariable(value = "moduleId") UUID courseId,
                                    @RequestBody @Valid ModuleDto moduleDto) {

        Optional<Module> moduleIntoCourse = moduleService.findAllModulesIntoCourse(moduleId, courseId);

        if (moduleIntoCourse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course");
        }

        var module = moduleIntoCourse.get();
        module.setTitle(moduleDto.title());
        module.setDescription(moduleDto.description());
        module.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(moduleService.create(module));
    }

    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<?> getOneCourse(@PathVariable(value = "moduleId") UUID moduleId,
                                          @PathVariable(value = "courseId") UUID courseId) {
        Optional<Module> moduleIntoCourse = moduleService.findAllModulesIntoCourse(moduleId, courseId);

        if (moduleIntoCourse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course");
        }

        return ResponseEntity.status(HttpStatus.OK).body(moduleIntoCourse.get());
    }


}