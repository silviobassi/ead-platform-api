package com.ead.course.controllers;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.Course;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDto courseDto){

        var course = new Course();
        BeanUtils.copyProperties(courseDto, course);
        course.setCreationDate(OffsetDateTime.now());
        course.setLastUpdateDate(OffsetDateTime.now());
        courseService.create(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value="courseId") UUID courseId){
        Optional<Course> courseOptional = courseService.findById(courseId);
        if(courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        courseService.delete(courseOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value="courseId") UUID courseId,
                                               @RequestBody @Valid CourseDto courseDto){
        Optional<Course> courseOptional = courseService.findById(courseId);
        if(courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found.");
        }
        var course = courseOptional.get();
        course.setName(courseDto.name());
        course.setDescription(courseDto.description());
        course.setImageUrl(courseDto.imageUrl());
        course.setCourseStatus(courseDto.courseStatus());
        course.setCourseLevel(courseDto.courseLevel());
        course.setLastUpdateDate(OffsetDateTime.now(ZoneId.of("UTC")));
        courseService.create(course);

        return ResponseEntity.status(HttpStatus.OK).body(course);
    }
    @GetMapping
    public ResponseEntity<Page<Course>> getAllCourses(SpecificationTemplate.CourseSpec spec,
                                                            @PageableDefault(size = 10, sort = "courseId", direction = Sort.Direction.ASC)
                                                            Pageable pageable, @RequestParam(required = false) UUID userId){
        Page<Course> coursePage;

        if(Objects.nonNull(userId)){
            coursePage = courseService.findAllCourses(SpecificationTemplate.courseUserId(userId).and(spec), pageable);
        } else {
            coursePage = courseService.findAllCourses(spec, pageable);
        }

        return ResponseEntity.status(HttpStatus.OK).body(coursePage);
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<Object> getOneCourse(@PathVariable(value="courseId") UUID courseId){
        Optional<Course> courseOptional = courseService.findById(courseId);
        return courseOptional.<ResponseEntity<Object>>map(course -> ResponseEntity.status(HttpStatus.OK)
                .body(course)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found."));
    }


}
