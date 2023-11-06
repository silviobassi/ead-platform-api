package com.ead.authuser.controllers;

import com.ead.authuser.configs.security.JwtProvider;
import com.ead.authuser.dtos.JwtDto;
import com.ead.authuser.dtos.LoginDto;
import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.RoleType;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.Role;
import com.ead.authuser.models.User;
import com.ead.authuser.services.RoleService;
import com.ead.authuser.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/auth")
public class AuthenticationController {
    final UserService userService;
    final RoleService roleService;
    final JwtProvider jwtProvider;
    final PasswordEncoder passwordEncoder;
    final AuthenticationManager authenticationManager;

    public AuthenticationController(UserService userService,
                                    RoleService roleService,
                                    JwtProvider jwtProvider,
                                    PasswordEncoder passwordEncoder,
                                    AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.roleService = roleService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody
                                          @Validated(UserDto.UserView.RegistrationPost.class)
                                          @JsonView(UserDto.UserView.RegistrationPost.class)
                                          UserDto userDto) {

        log.debug("POST registerUser userDTO received {} ", userDto.toString());

        if (userService.existsByUserName(userDto.userName())) {
            log.warn("UserName {} is Already Taken ", userDto.userName());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username is Already Taken!");
        }

        if (userService.existsByEmail(userDto.email())) {
            log.warn("Email {} is Already Taken ", userDto.email());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }

        Role role = roleService.findByRoleName(RoleType.ROLE_STUDENT);
        String password = passwordEncoder.encode(userDto.password());

        var userModel = new User();
        BeanUtils.copyProperties(userDto, userModel);
        userModel.setPassword(password);
        userModel.setUserStatus(UserStatus.ACTIVE);
        userModel.setUserType(UserType.STUDENT);
        userModel.setCreationDate(OffsetDateTime.now(ZoneId.of("UTC")));
        userModel.setLastUpdateDate(OffsetDateTime.now(ZoneId.of("UTC")));

        userModel.getRoles().add(role);
        userService.saveUser(userModel);

        log.debug("POST registerUser userId saved {} ", userModel.getUserId());
        log.info("User saved successfully userId {} ", userModel.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.userName(), loginDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }

    @GetMapping
    public String index() {
        log.trace("TRACE");
        log.debug("DEBUG");
        log.info("INFO");
        log.warn("WARN");
        log.error("ERROR");
        return "Logging Spring Boot...";
    }
}
