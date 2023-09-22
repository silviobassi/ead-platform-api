package com.authuser.authuser.controllers;

import com.authuser.authuser.dtos.UserModelDto;
import com.authuser.authuser.models.UserModel;
import com.authuser.authuser.services.UserService;
import com.authuser.authuser.specifications.SpecificationsTemplate;
import com.fasterxml.jackson.annotation.JsonView;
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
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<UserModel> getAllUsers(SpecificationsTemplate.UserSpec spec,
                                       @PageableDefault(size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable){
        Page<UserModel> userModelPage = userService.findAll(spec, pageable);
        if(!userModelPage.isEmpty()){
            for (UserModel user: userModelPage) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getUserId())).withSelfRel());
            }
        }
        return userModelPage;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{userId}")
    public ResponseEntity<?> getOneUser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userCurrent);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "userId") UUID userId){
        Optional<UserModel> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.delete(userCurrent.get());
        return ResponseEntity.noContent().build();

    }

    @PutMapping ("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "userId") UUID userId,
                                        @RequestBody
                                        @Validated(UserModelDto.UserView.UserPut.class)
                                        @JsonView(UserModelDto.UserView.UserPut.class) UserModelDto userModelDto){
        Optional<UserModel> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userModel = userCurrent.get();
        userModel.setFullName(userModelDto.fullName());
        userModel.setPhoneNumber(userModelDto.phoneNumber());
        userModel.setCpf(userModelDto.cpf());
        userModel.setLastUpdateDate(OffsetDateTime.now());

        return ResponseEntity.status(HttpStatus.OK).body(userService.create(userModel));

    }

    @PutMapping ("/{userId}/password")
    public ResponseEntity<?> updatePassword(@PathVariable(value = "userId") UUID userId,
                                            @RequestBody
                                            @Validated(UserModelDto.UserView.PasswordPut.class)
                                            @JsonView(UserModelDto.UserView.PasswordPut.class) UserModelDto userModelDto){
        Optional<UserModel> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } if(!userCurrent.get().getPassword().equals(userModelDto.oldPassword())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Mismatched old password!");
        }

        var userModel = userCurrent.get();
        userModel.setPassword(userModelDto.password());
        userModel.setLastUpdateDate(OffsetDateTime.now());
        userService.create(userModel);
        return ResponseEntity.noContent().build();

    }

    @PutMapping ("/{userId}/image")
    public ResponseEntity<?> updateImage(@PathVariable(value = "userId") UUID userId,
                                         @RequestBody
                                         @Validated(UserModelDto.UserView.ImagePut.class)
                                         @JsonView(UserModelDto.UserView.ImagePut.class) UserModelDto userModelDto){
        Optional<UserModel> userCurrent = userService.findById(userId);
        if(userCurrent.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        var userModel = userCurrent.get();
        userModel.setImageUrl(userModelDto.imageUrl());
        userModel.setLastUpdateDate(OffsetDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(userService.create(userModel));

    }


}
