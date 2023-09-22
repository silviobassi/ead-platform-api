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
@RequestMapping("/modules")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;


    @GetMapping("/{courseId}/courses")
    public ResponseEntity<?> getAllModules(@PathVariable(name = "courseId") UUID courseId){
        var courseCurrent = courseService.findById(courseId);

        if(courseCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(moduleService.findAllByCourse(courseCurrent.get()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
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

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<?> delete(@PathVariable(value = "moduleId") UUID moduleId) {
        Optional<Module> moduleCurrent = moduleService.findById(moduleId);
        if (moduleCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }
        moduleService.delete(moduleCurrent.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<?> update(@PathVariable(value = "moduleId") UUID moduleId,
                                    @RequestBody @Valid ModuleDto moduleDto) {

        Optional<Module> moduleCurrent = moduleService.findById(moduleId);
        Optional<Course> courseCurrent = courseService.findById(moduleDto.courseId());
        if (moduleCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }

        if (courseCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        var module = moduleCurrent.get();
        module.setTitle(moduleDto.title());
        module.setDescription(moduleDto.description());
        module.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        courseCurrent.ifPresent(module::setCourse);

        return ResponseEntity.status(HttpStatus.OK).body(moduleService.create(module));
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<?> getOneCourse(@PathVariable(value = "moduleId") UUID moduleId) {
        Optional<Module> moduleCurrent = moduleService.findById(moduleId);
        if (moduleCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(moduleCurrent.get());
    }


}
