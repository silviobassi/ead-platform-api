package com.ead.course.controllers;

import com.ead.course.dtos.LessonDto;
import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;

    @GetMapping("/modules/{moduleId}/lessons")
    public ResponseEntity<?> getAllLessons(@PathVariable(name = "moduleId") UUID moduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.findAllLessonsIntoModule(moduleId));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/modules")
    public ResponseEntity<?> create(@RequestBody @Valid LessonDto lessonDto) {
        Optional<Module> moduleCurrent = moduleService.findById(lessonDto.moduleId());
        if (moduleCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }
        var lesson = new Lesson();
        BeanUtils.copyProperties(lessonDto, lesson);
        lesson.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        lesson.setTitle(lessonDto.title());
        lesson.setDescription(lessonDto.description());
        lesson.setVideoUrl(lessonDto.videoUrl());
        lesson.setModule(moduleCurrent.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.create(lesson));
    }

    @DeleteMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> delete(@PathVariable(value = "moduleId") UUID moduleId,
                                    @PathVariable(value = "lessonId") UUID lessonId) {
        Optional<Lesson> lessonIntoModule = lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lessonIntoModule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module");
        }
        lessonService.delete(lessonIntoModule.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> update(@PathVariable(value = "moduleId") UUID moduleId,
                                    @PathVariable(value = "lessonId") UUID lessonId,
                                    @RequestBody @Valid LessonDto lessonDto) {

        Optional<Lesson> lessonIntoModule = lessonService.findLessonIntoModule(moduleId, lessonId);

        if (lessonIntoModule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module");
        }

        var lesson = lessonIntoModule.get();
        lesson.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        lesson.setTitle(lessonDto.title());
        lesson.setDescription(lessonDto.description());
        lesson.setVideoUrl(lessonDto.videoUrl());

        return ResponseEntity.status(HttpStatus.OK).body(lessonService.create(lesson));
    }

    @GetMapping("/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<?> getOneCourse(@PathVariable(value = "moduleId") UUID moduleId,
                                          @PathVariable(value = "lessonId") UUID lessonId) {
        Optional<Lesson> lessonIntoModule = lessonService.findLessonIntoModule(lessonId, moduleId);

        if (lessonIntoModule.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found for this module");
        }

        return ResponseEntity.status(HttpStatus.OK).body(lessonIntoModule.get());
    }
}
