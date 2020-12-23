package com.example.authentic.repository;

import com.example.authentic.model.UserEntitiy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository {

    Optional<UserEntitiy> findByUsername(String username);

    Boolean existsByUsername(String username);
}
