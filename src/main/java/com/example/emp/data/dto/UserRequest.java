package com.example.emp.data.dto;

import com.example.common.validator.MultiFieldUnique;
import com.example.emp.data.entity.UserEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@MultiFieldUnique(clazz = UserEntity.class, uniqueFields = "username,email", idField = "userId", message = "{validation.unique.user}")
public class UserRequest {
    private Long userId;
    @NotEmpty(message = "{validation.user.username.notnull}")
    private String username;
    @NotEmpty(message = "{validation.user.password.notnull}")
    private String password;
    @NotEmpty(message = "{validation.user.email.notnull}")
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private String[] roleInputList;
}
