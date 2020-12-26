package com.example.authentic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotNull(message = "{language.valid.user.username}")
    private String username;

    @NotNull(message = "{language.valid.user.password}")
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

    public UserEntity toEntity() {
        return new UserEntity(
                id,
                username,
                password,
                email
        );
    }
}
