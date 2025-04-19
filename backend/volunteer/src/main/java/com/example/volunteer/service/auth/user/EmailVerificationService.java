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

import java.time.Instant;

/**
 * Сервис для управления верификацией email пользователя.
 */
@Service
public class EmailVerificationService {

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Value("${app.frontend.email-verification.path}")
    private String frontendVerificationPath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthEmailService authEmailService;

    /**
     * Отправляет письмо с ссылкой для верификации email пользователя.
     * Использует существующий токен пользователя или генерирует новый.
     * Ссылка ведет на фронтенд.
     *
     * @param user Пользователь, которому нужно отправить письмо.
     */
    public void sendVerificationEmail(User user) {
        String token = user.getAuthToken();
        // Получаем Instant напрямую
        Instant tokenCreationInstant = user.getTokenCreatedAt();

        // Передаем Instant в isTokenExpired
        if (token == null || tokenService.isTokenExpired(tokenCreationInstant)) {
            token = tokenService.generateToken(user);
        }

        String verificationLink = frontendBaseUrl + frontendVerificationPath + "/" + token;
        authEmailService.sendRegistrationLink(user.getEmail(), verificationLink);
    }

    /**
     * Верифицирует email пользователя по предоставленному токену.
     * Активирует учетную запись пользователя, если токен валиден и не истек.
     *
     * @param token Токен верификации.
     * @return Верифицированный объект User.
     * @throws InvalidAuthToken      если токен не найден или недействителен.
     * @throws TokenExpiredException если срок действия токена истек.
     */
    public User verifyEmail(String token) {
        // validateToken теперь корректно работает с Instant
        User user = tokenService.validateToken(token);

        user.setEnabled(true);
        tokenService.clearToken(user);
        userRepository.save(user);
        return user;
    }

    /**
     * Повторно отправляет письмо для верификации email.
     * Генерирует новый токен перед отправкой.
     *
     * @param email Email пользователя для повторной отправки.
     */
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("Email уже подтвержден");
        }

        // generateToken устанавливает Instant.now()
        tokenService.generateToken(user);
        // sendVerificationEmail теперь корректно работает с Instant
        sendVerificationEmail(user);
    }
}