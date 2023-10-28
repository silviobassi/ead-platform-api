package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.models.Course;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {
    final CourseService courseService;

    final UserService courseUserService;

    public CourseUserController(CourseService courseService, UserService courseUserService) {
        this.courseService = courseService;
        this.courseUserService = courseUserService;
    }

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(@PageableDefault(size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                      @PathVariable(value = "courseId") UUID courseId) {

        Optional<Course> courseOptional = courseService.findById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("Course Not Found");
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<?> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                          @RequestBody @Valid SubscriptionDto subscriptionDto) {

        Optional<Course> courseOptional = courseService.findById(courseId);
        if (courseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        // State Transfer Verification
        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }

}
