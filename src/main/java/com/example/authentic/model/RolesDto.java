package com.example.authentic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolesDto {
    private Long id;
    private ERole code;

    private String rolesInput;

    public RolesDto(Long id, ERole code) {
        this.id = id;
        this.code = code;
    }
}
