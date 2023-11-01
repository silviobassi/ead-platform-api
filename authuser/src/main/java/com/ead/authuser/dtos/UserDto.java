package com.ead.authuser.dtos;

import com.ead.authuser.validation.UserNameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDto(UUID userId,
                      @NotBlank(groups = UserView.RegistrationPost.class)
                           @Size(min = 4, max = 50)
                           @UserNameConstraint(groups = UserView.RegistrationPost.class)
                           @JsonView(UserView.RegistrationPost.class)
                           String userName,

                      @NotBlank(groups = UserView.RegistrationPost.class)
                           @Email(groups = UserView.RegistrationPost.class)
                           @JsonView(UserView.RegistrationPost.class)
                           String email,
                      @Size(min = 6, max = 20, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
                           @NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
                           @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
                           String password,
                      @Size(min = 6, max = 20, groups = UserView.PasswordPut.class)
                           @NotBlank(groups = UserView.PasswordPut.class)
                           @JsonView(UserView.PasswordPut.class)
                           String oldPassword,
                      @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
                           String fullName,
                      @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
                           String phoneNumber,
                      @CPF(groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
                      @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
                           String cpf,
                      @NotBlank(groups = UserView.ImagePut.class)
                           @JsonView(UserView.ImagePut.class)
                           String imageUrl
) implements Serializable {

    public interface UserView {
        public static interface RegistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }

}