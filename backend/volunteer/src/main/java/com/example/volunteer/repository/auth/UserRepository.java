package com.example.volunteer.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.volunteer.model.auth.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAuthToken(String authToken);
    boolean existsByAuthToken(String authToken);
}