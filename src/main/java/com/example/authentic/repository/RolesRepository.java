package com.example.authentic.repository;

import com.example.authentic.model.ERole;
import com.example.authentic.model.RolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<RolesEntity, Long> {
    Optional<RolesEntity> findByName(ERole name);
}
