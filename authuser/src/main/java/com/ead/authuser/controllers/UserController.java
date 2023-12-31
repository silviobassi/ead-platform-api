package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.models.User;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationsTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(SpecificationsTemplate.UserSpec spec,
                                  @PageableDefault(size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable,
                                  @RequestParam(required = false) UUID courseId){
        Page<User> userModelPage;

        if(Objects.nonNull(courseId)){
            userModelPage = userService.findAll(SpecificationsTemplate.userCourseId(courseId).and(spec), pageable);
        } else {
            userModelPage = userService.findAll(spec, pageable);
        }

        if(!userModelPage.isEmpty()){
            for (User user: userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{userId}")
    public ResponseEntity<?> getOneUser(@PathVariable(value = "userId") UUID userId){
        Optional<User> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userCurrent);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") UUID userId){
        Optional<User> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.delete(userCurrent.get());
        return ResponseEntity.noContent().build();

    }

    @PutMapping ("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "userId") UUID userId,
                                        @RequestBody
                                        @Validated(UserDto.UserView.UserPut.class)
                                        @JsonView(UserDto.UserView.UserPut.class) UserDto userDto){
        log.debug("PUT updateUser userDTO received {} ", userDto.toString());

        Optional<User> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userModel = userCurrent.get();
        userModel.setFullName(userDto.fullName());
        userModel.setPhoneNumber(userDto.phoneNumber());
        userModel.setCpf(userDto.cpf());
        userModel.setLastUpdateDate(OffsetDateTime.now());
        userService.create(userModel);

        log.debug("PUT updateUser userId update {} ", userModel.getUserId());
        log.info("User saved successfully userId {} ", userModel.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(userModel);

    }

    @PutMapping ("/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable(value = "userId") UUID userId,
                                            @RequestBody
                                            @Validated(UserDto.UserView.PasswordPut.class)
                                            @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto){
        Optional<User> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } if(!userCurrent.get().getPassword().equals(userDto.oldPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }

        var userModel = userCurrent.get();
        userModel.setPassword(userDto.password());
        userModel.setLastUpdateDate(OffsetDateTime.now());
        userService.create(userModel);
        return ResponseEntity.noContent().build();

    }

    @PutMapping ("/{userId}/image")
    public ResponseEntity<?> updateImage(@PathVariable(value = "userId") UUID userId,
                                         @RequestBody
                                         @Validated(UserDto.UserView.ImagePut.class)
                                         @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto){
        Optional<User> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userModel = userCurrent.get();
        userModel.setImageUrl(userDto.imageUrl());
        userModel.setLastUpdateDate(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(userService.create(userModel));

    }


}
