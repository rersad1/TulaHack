package com.example.volunteer.repository.auth;

import com.example.volunteer.model.auth.JwtRefreshToken;
import com.example.volunteer.model.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRefreshTokenRepository extends JpaRepository<JwtRefreshToken, Long> {
    Optional<JwtRefreshToken> findByToken(String token);
    void deleteByUser(User user);
    void deleteById(Long id);
    void deleteByToken(String token);
}