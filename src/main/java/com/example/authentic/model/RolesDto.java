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
    private ERole name;

    private String rolesInput;

    public RolesDto(Long id, ERole name) {
        this.id = id;
        this.name = name;
    }

    public RolesEntity toEntity() {
        return new RolesEntity(
                id,
                name
        );
    }
}
