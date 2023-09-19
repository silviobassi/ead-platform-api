package com.authuser.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserModelDto(UUID userId,
                           @JsonView(UserView.RegistrationPost.class)
                           String userName,
                           @JsonView(UserView.RegistrationPost.class)
                           String email,
                           @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
                           String password,
                           @JsonView(UserView.PasswordPut.class)
                           String oldPassword,
                           @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
                           String fullName,
                           @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
                           String phoneNumber,
                           @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
                           String cpf,
                           @JsonView({UserView.RegistrationPost.class, UserView.ImagePut.class})
                           String imageUrl) implements Serializable {

    public interface UserView {
        public static interface RegistrationPost{}
        public static interface UserPut{}
        public static interface PasswordPut{}
        public static interface ImagePut{}
    }
}