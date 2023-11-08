package com.ead.authuser.controllers;

import com.ead.authuser.dtos.InstructorDto;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.Role;
import com.ead.authuser.models.User;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Optional;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/instructors")
public class InstructorController {

    final UserService userService;
    final RoleService roleService;

    public InstructorController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/subscription")
    public ResponseEntity<?> saveSubscriptionInstructor(@RequestBody @Valid InstructorDto instructorDto) {
        Optional<User> userOptional = userService.findById(instructorDto.userId());
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }

        Role role = roleService.findByRoleName(RoleType.ROLE_INSTRUCTOR);

        User user = userOptional.get();
        user.setUserType(UserType.INSTRUCTOR);
        user.setLastUpdateDate(OffsetDateTime.now());
        user.getRoles().add(role);
        userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
