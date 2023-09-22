package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.Course;
import com.ead.course.services.CourseService;
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
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Course> getAllCourses(){
        return courseService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Course create(@RequestBody @Valid CourseDto courseDTO) {
        var course = new Course();
        BeanUtils.copyProperties(courseDTO, course);
        course.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        course.setLastUpdateDate(OffsetDateTime.now(ZoneId.of("UTC")));
        return courseService.create(course);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<?> delete(@PathVariable(value = "courseId") UUID courseId) {
        Optional<Course> courseCurrent = courseService.findById(courseId);
        if (courseCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        courseService.delete(courseCurrent.get());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<?> update(@PathVariable(value = "courseId") UUID courseId,
                                    @RequestBody @Valid CourseDto courseDTO) {

        Optional<Course> courseCurrent = courseService.findById(courseId);
        if (courseCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }

        var course = courseCurrent.get();
        course.setName(courseDTO.name());
        course.setDescription(courseDTO.description());
        course.setImageUrl(courseDTO.imageUrl());
        course.setCourseStatus(courseDTO.courseStatus());
        course.setCourseLevel(courseDTO.courseLevel());
        course.setLastUpdateDate(OffsetDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(courseService.create(course));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<?> getOneCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<Course> courseCurrent = courseService.findById(courseId);
        if (courseCurrent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(courseCurrent.get());
    }
}
