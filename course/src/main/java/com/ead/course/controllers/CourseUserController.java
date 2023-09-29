package com.ead.course.controllers;

import com.ead.course.UserDto;
import com.ead.course.clients.CourseClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.models.Course;
import com.ead.course.models.CourseUser;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
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

    final CourseClient courseClient;

    final CourseService courseService;

    final CourseUserService courseUserService;

    public CourseUserController(
            CourseClient userClient,
            CourseService courseService,
            CourseUserService courseUserService) {

        this.courseClient = userClient;
        this.courseService = courseService;
        this.courseUserService = courseUserService;
    }


    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDto>> getAllUserByCourse(@PageableDefault(size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                             @PathVariable(value = "courseId") UUID courseId) {

        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllUserByCourse(courseId, pageable));
    }


    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<?> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId,
                                                          @RequestBody @Valid SubscriptionDto subscriptionDto){
        Optional<Course> courseOptional = courseService.findById(courseId);

        if(courseOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }

        if(courseUserService.existsByCourseAndUserId(courseOptional.get(), subscriptionDto.userId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: subscription already exists");
        }
        CourseUser courseUser =  courseUserService.save(courseOptional.get().convertToCourseUser(subscriptionDto.userId()));
        return ResponseEntity.status(HttpStatus.CREATED).body("Subscription created successfully.");
    }


}
