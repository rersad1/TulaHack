package com.example.volunteer.service.auth.user;

import com.example.volunteer.exceptions.auth.InvalidAuthToken;
import com.example.volunteer.exceptions.auth.TokenExpiredException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import com.example.volunteer.service.auth.token.TokenService;
import com.example.volunteer.service.email.AuthEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailVerificationService {

    @Value("${app.base-url}${app.email-verification.path}") 
    private String emailVerificationUrlBase;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private AuthEmailService authEmailService;

    public void sendVerificationEmail(User user) {
        // Используем существующий токен или создаем новый, если он отсутствует
        String token = user.getAuthToken();
        if (token == null) {
            token = tokenService.generateToken(user);
        }
        
        String verificationLink = emailVerificationUrlBase + token;
        authEmailService.sendRegistrationLink(user.getEmail(), verificationLink);
    }
    
    public void verifyEmail(String token) {
        token = token.replace("\"", "");
        
        User user = userRepository.findByAuthToken(token).orElseThrow(() -> new InvalidAuthToken());
        
        // Проверка срока действия токена
        if (tokenService.isTokenExpired(user.getTokenCreatedAt())) {
            tokenService.clearToken(user);
            throw new TokenExpiredException();
        }
        
        // Активируем учетную запись пользователя
        user.setEnabled(true);
        userRepository.save(user);
    }
    
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Пользователь не найден"));
                
        if (Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("Email уже подтвержден");
        }
        
        // Очищаем старый токен и создаем новый
        tokenService.clearToken(user);
        tokenService.generateToken(user);
        
        sendVerificationEmail(user);
    }
}