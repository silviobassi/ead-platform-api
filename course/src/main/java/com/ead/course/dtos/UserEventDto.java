package com.ead.course.dtos;

import com.ead.course.models.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Data
public class UserEventDto {
    private UUID userId;
    private String userName;
    private String email;
    private String fullName;
    private String userStatus;
    private String userType;
    private String phoneNumber;
    private String cpf;
    private String imageUrl;
    private String actionType;

    public User convertToUser(){
        var user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

}
