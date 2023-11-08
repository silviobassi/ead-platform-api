package com.ead.course.controllers;


import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.Course;
import com.ead.course.models.Module;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping("/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@PathVariable(value="courseId") UUID courseId,
                                             @RequestBody @Valid ModuleDto moduleDto){
        Optional<Course> courseOptional = courseService.findById(courseId);
        if(courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        var module = new Module();
        BeanUtils.copyProperties(moduleDto, module);
        module.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        module.setCourse(courseOptional.get());
        moduleService.create(module);
        return ResponseEntity.status(HttpStatus.CREATED).body(module);
    }
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteModule(@PathVariable(value="courseId") UUID courseId,
                                               @PathVariable(value="moduleId") UUID moduleId){
        Optional<Module> moduleModelOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(moduleModelOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        moduleService.delete(moduleModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully.");
    }
    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable(value="courseId") UUID courseId,
                                               @PathVariable(value="moduleId") UUID moduleId,
                                               @RequestBody @Valid ModuleDto moduleDto){
        log.debug("PUT updateModule moduleDto received {} ", moduleDto.toString());
        Optional<Module> moduleOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        if(moduleOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course.");
        }
        var module = moduleOptional.get();
        module.setTitle(moduleDto.title());
        module.setDescription(moduleDto.description());
        moduleService.create(module);
        log.debug("PUT updateModule moduleId saved {} ", module.getModuleId());
        log.info("Module updated successfully moduleId {} ", module.getModuleId());
        return ResponseEntity.status(HttpStatus.OK).body(module);
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/courses/{courseId}/modules")
    public ResponseEntity<Page<Module>> getAllModules(@PathVariable(value="courseId") UUID courseId,
                                                      SpecificationTemplate.ModuleSpec spec,
                                                      @PageableDefault(size = 10, sort = "moduleId",
                                                              direction = Sort.Direction.ASC) Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(moduleService
                .findAllByCourse(SpecificationTemplate.moduleCourseId(courseId).and(spec), pageable));
    }


    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> getOneModule(@PathVariable(value="courseId") UUID courseId,
                                               @PathVariable(value="moduleId") UUID moduleId){
        Optional<Module> moduleOptional = moduleService.findModuleIntoCourse(courseId, moduleId);
        return moduleOptional.<ResponseEntity<Object>>map(module -> ResponseEntity.status(HttpStatus.OK)
                .body(module)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Module not found for this course."));
    }

}
