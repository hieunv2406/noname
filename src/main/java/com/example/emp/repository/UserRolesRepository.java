package com.example.emp.repository;

import com.example.emp.data.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRolesRepository extends JpaRepository<UserRoleEntity, Long> {
}
