package com.example.volunteer.service.auth.password;

import com.example.volunteer.exceptions.auth.NonExistsUserException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import com.example.volunteer.service.auth.token.TokenService;
import com.example.volunteer.service.email.AuthEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Сервис для сброса пароля через почту

@Service
public class PasswordResetService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthEmailService emailService;
    
    @Autowired
    private TokenService tokenService;

    @Value("${app.base-url}${app.password-reset.path}")
    private String passwordResetUrlBase;
    
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NonExistsUserException());
        
        String resetToken = tokenService.generateToken(user);
        
        String resetLink = passwordResetUrlBase + resetToken;
        emailService.sendPasswordResetToken(email, resetLink);
    }
    
    public void resetPassword(String token, String newPassword) {
        User user = tokenService.validateToken(token);
        
        PasswordValidator.validate(newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        
        user.setPassword(encodedPassword);
        tokenService.clearToken(user);
    }
}