package com.authuser.authuser.controllers;

import com.authuser.authuser.dtos.UserModelDto;
import com.authuser.authuser.enums.UserStatus;
import com.authuser.authuser.enums.UserType;
import com.authuser.authuser.models.UserModel;
import com.authuser.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.TimeZone;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody
                                              @Validated(UserModelDto.UserView.RegistrationPost.class)
                                              @JsonView(UserModelDto.UserView.RegistrationPost.class)
                                              UserModelDto userModelDto){

        if(userService.existsByUserName(userModelDto.userName())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }

        if(userService.existsByEmail(userModelDto.email())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }

        var userModel = new UserModel();
        BeanUtils.copyProperties(userModelDto, userModel);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(OffsetDateTime.now());
        userModel.setLastUpdateDate(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(userService.create(userModel));
    }
}
