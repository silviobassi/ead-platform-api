package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import com.ead.course.services.LessonService;
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
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PostMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@PathVariable(value="moduleId") UUID moduleId,
                                             @RequestBody @Valid LessonDto lessonDto){

        Optional<Module> moduleOptional = moduleService.findById(moduleId);
        if(moduleOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found.");
        }
        var lesson= new Lesson();
        BeanUtils.copyProperties(lessonDto, lesson);
        lesson.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        lesson.setModule(moduleOptional.get());
        lessonService.create(lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable(value="moduleId") UUID moduleId,
                                               @PathVariable(value="lessonId") UUID lessonId){
        Optional<Lesson> lessonOptional = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        lessonService.delete(lessonOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body("Lesson deleted successfully.");
    }

    @PreAuthorize("hasAnyRole('INSTRUCTOR')")
    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable(value="moduleId") UUID moduleId,
                                               @PathVariable(value="lessonId") UUID lessonId,
                                               @RequestBody @Valid LessonDto lessonDto){
        Optional<Lesson> lessonIntoModule = lessonService.findLessonIntoModule(moduleId, lessonId);
        if(lessonIntoModule.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module.");
        }
        var lesson = lessonIntoModule.get();
        lesson.setTitle(lessonDto.title());
        lesson.setDescription(lessonDto.description());
        lesson.setVideoUrl(lessonDto.videoUrl());
        lessonService.create(lesson);

        return ResponseEntity.status(HttpStatus.OK).body(lesson);
    }


    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<Page<Lesson>> getAllLessons(@PathVariable(value="moduleId") UUID moduleId,
                                                      SpecificationTemplate.LessonSpec spec,
                                                      @PageableDefault(size = 10, sort = "lessonId",
                                                              direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService
                .findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec), pageable));
    }

    @PreAuthorize("hasAnyRole('STUDENT')")
    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> getOneLesson(@PathVariable(value="moduleId") UUID moduleId,
                                               @PathVariable(value="lessonId") UUID lessonId){
        Optional<Lesson> lessonIntoModule = lessonService.findLessonIntoModule(moduleId, lessonId);
        return lessonIntoModule.<ResponseEntity<Object>>map(lesson -> ResponseEntity.status(HttpStatus.OK)
                .body(lesson)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Lesson not found for this module."));
    }
}
