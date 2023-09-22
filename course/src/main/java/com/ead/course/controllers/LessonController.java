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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/lessons")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{moduleId}/modules")
    public ResponseEntity<?> getAllLessons(@PathVariable(name = "moduleId") UUID moduleId){
        Optional<Module> moduleCurrent = moduleService.findById(moduleId);

        if(moduleCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(lessonService.findAllByModule(moduleCurrent.get()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid LessonDto lessonDto) {
        Optional<Module> moduleCurrent = moduleService.findById(lessonDto.moduleId());
        if (moduleCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }
        var lesson = new Lesson();
        BeanUtils.copyProperties(lessonDto, lesson);
        lesson.setTitle(lessonDto.title());
        lesson.setDescription(lessonDto.description());
        lesson.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        lesson.setModule(moduleCurrent.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.create(lesson));
    }

    @DeleteMapping("/{lessonId}")
    public ResponseEntity<?> delete(@PathVariable(value = "lessonId") UUID lessonId) {
        Optional<Lesson> moduleCurrent = lessonService.findById(lessonId);
        if (moduleCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
        }
        lessonService.delete(moduleCurrent.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<?> update(@PathVariable(value = "lessonId") UUID lessonId,
                                    @RequestBody @Valid LessonDto lessonDto) {

        Optional<Lesson> lessonCurrent = lessonService.findById(lessonId);
        Optional<Module> moduleCurrent = moduleService.findById(lessonDto.moduleId());

        if (lessonCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
        }

        if (moduleCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found");
        }

        var lesson = lessonCurrent.get();
        lesson.setTitle(lessonDto.title());
        lesson.setDescription(lessonDto.description());
        lesson.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        moduleCurrent.ifPresent(lesson::setModule);

        return ResponseEntity.status(HttpStatus.OK).body(lessonService.create(lesson));
    }

    @GetMapping("/{lessonId}")
    public ResponseEntity<?> getOneCourse(@PathVariable(value = "lessonId") UUID lessonId) {
        Optional<Lesson> lessonCurrent = lessonService.findById(lessonId);
        if (lessonCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(lessonCurrent.get());
    }
}
