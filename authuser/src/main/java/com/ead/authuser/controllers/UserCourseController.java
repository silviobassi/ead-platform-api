package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.models.User;
import com.ead.authuser.models.UserCourse;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
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
public class UserCourseController {

    final CourseClient courseClient;
    final UserService userService;
    final UserCourseService userCourseService;

    public UserCourseController(CourseClient courseClient,
                                UserService userService,
                                UserCourseService userCourseService) {
        this.courseClient = courseClient;
        this.userService = userService;
        this.userCourseService = userCourseService;
    }


    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseDto>> getAllCourserByUser(@PageableDefault(size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable,
                                                               @PathVariable(value = "userId") UUID userId) {


        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, pageable));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<?> saveSubscriptionUserInCourse(@PathVariable UUID userId,
                                                          @RequestBody @Valid UserCourseDto userCourseDto) {
        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        if (userCourseService.existsByUserAndCourseId(userOptional.get(), userCourseDto.courseId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Subscription already exists!");
        }

        UserCourse userCourse = userCourseService.save(userOptional.get().convertToUserCourse(userCourseDto.courseId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourse);
    }

    @DeleteMapping("/users/courses/{courseId}")
    public ResponseEntity<?> deleteUserCourseByCourse(@PathVariable(value = "courseId") UUID courseId){
        if(!userCourseService.existsByCourseId(courseId)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserCourse not found");
        }
        userCourseService.deleteUserCourseByCourse(courseId);
        return ResponseEntity.status(HttpStatus.OK).body("UserCourse deleted successfully.");
    }

}
