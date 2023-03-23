package com.example.authentic.repository;

import com.example.authentic.model.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoleEntity, Long> {
}
