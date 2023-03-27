package com.example.emp.data.dto;

import com.example.common.validator.MultiFieldUnique;
import com.example.emp.data.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MultiFieldUnique(clazz = UserEntity.class, uniqueFields = "username,email", idField = "id", message = "{validation.unique.user}")
public class UserDto {
    private Long id;

    @NotEmpty(message = "{validation.user.username.notnull}")
    private String username;

    @NotEmpty(message = "{validation.user.password.notnull}")
    private String password;

    private String email;

    private Set<RolesDto> roles = new HashSet<>();

    private String[] lstRoleInput;

    public UserDto(Long id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
