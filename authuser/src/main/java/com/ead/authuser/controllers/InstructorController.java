package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.User;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Optional;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    final UserService userService;

    public InstructorController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/subscription")
    public ResponseEntity<?> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto) {
        Optional<User> userOptional = userService.findById(instructorDto.userId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        User user = userOptional.get();
        user.setUserType(UserType.INSTRUCTOR);
        user.setLastUpdateDate(OffsetDateTime.now());
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
