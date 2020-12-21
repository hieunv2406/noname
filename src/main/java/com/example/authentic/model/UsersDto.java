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
public class UsersDto {
    @NotNull(message = "{language.valid.user.username}")
    private String username;
    @NotNull(message = "{language.valid.user.password}")
    private String password;
    private String email;
}
