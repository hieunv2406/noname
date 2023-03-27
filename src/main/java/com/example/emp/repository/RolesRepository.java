package com.example.emp.repository;

import com.example.emp.data.entity.ERole;
import com.example.emp.data.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByCode(ERole code);

    Optional<RoleEntity> findById(Long id);
}
