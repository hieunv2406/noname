package com.example.authentic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

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

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserEntitiy toEntity() {
        return new UserEntitiy(
                id,
                username,
                password
        );
    }
}
