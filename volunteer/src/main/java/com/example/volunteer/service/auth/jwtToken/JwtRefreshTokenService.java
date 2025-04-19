package com.example.volunteer.service.auth.jwtToken;

import com.example.volunteer.model.auth.JwtRefreshToken;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.JwtRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class JwtRefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    @Autowired
    private JwtRefreshTokenRepository refreshTokenRepository;

    public JwtRefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUser(user); // Один refresh токен на пользователя

        JwtRefreshToken refreshToken = new JwtRefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_DAYS));
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<JwtRefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean isExpired(JwtRefreshToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    public void delete(JwtRefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    public void deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
    }
    
}