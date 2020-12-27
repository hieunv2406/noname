package com.example.authentic.repository;

import com.example.authentic.model.UserRolesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRolesRepository extends JpaRepository<UserRolesEntity, Long> {
    List<UserRolesEntity> findByUserId(Long userId);
}
