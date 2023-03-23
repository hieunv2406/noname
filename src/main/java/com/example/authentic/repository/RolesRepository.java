package com.example.authentic.repository;

import com.example.authentic.model.ERole;
import com.example.authentic.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByCode(ERole code);

    Optional<RoleEntity> findById(Long id);
}
