package com.ead.course.controllers;

import com.ead.course.UserDto;
import com.ead.course.clients.CourseClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    final CourseClient courseClient;

    public CourseUserController(CourseClient userClient) {
        this.courseClient = userClient;
    }


    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDto>> getAllUserByCourse(@PageableDefault(size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                                             @PathVariable(value = "courseId") UUID courseId) {

        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllUserByCourse(courseId, pageable));
    }

}
