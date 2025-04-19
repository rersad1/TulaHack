package com.example.volunteer.service.auth.token;

import com.example.volunteer.exceptions.auth.InvalidAuthToken;
import com.example.volunteer.exceptions.auth.TokenExpiredException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    private static final long TOKEN_EXPIRATION_MINUTES = 15;

    @Autowired
    private UserRepository userRepository;

    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();
        
        user.setAuthToken(token);
        user.setTokenCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        return token;
    }
    
    public User validateToken(String token) {
        
        token = token.replace("\"", "");
        
        User user = userRepository.findByAuthToken(token).orElseThrow(() -> new InvalidAuthToken());
        
        // Проверка срока действия токена
        if (isTokenExpired(user.getTokenCreatedAt())) {
            clearToken(user);
            throw new TokenExpiredException();
        }
        
        return user;
    }
    
    public void clearToken(User user) {
        user.setAuthToken(null);
        user.setTokenCreatedAt(null);
        userRepository.save(user);
    }
    
    public boolean isTokenExpired(LocalDateTime tokenCreatedAt) {
        if (tokenCreatedAt == null) {
            return true;
        }
        
        LocalDateTime expirationTime = tokenCreatedAt.plusMinutes(TOKEN_EXPIRATION_MINUTES);
        return LocalDateTime.now().isAfter(expirationTime);
    }
}