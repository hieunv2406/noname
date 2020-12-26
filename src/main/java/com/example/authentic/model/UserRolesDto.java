package com.example.authentic.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRolesDto {

    private Long id;
    private Long userId;
    private Long rolesId;

    public UserRolesEntity toEntity() {
        return new UserRolesEntity(
                id,
                userId,
                rolesId
        );
    }

}
