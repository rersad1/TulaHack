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

/**
 * Сервис для управления верификацией email пользователя.
 */
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

    /**
     * Отправляет письмо с ссылкой для верификации email пользователя.
     * Использует существующий токен пользователя или генерирует новый.
     *
     * @param user Пользователь, которому нужно отправить письмо.
     */
    public void sendVerificationEmail(User user) {
        // Используем существующий токен или создаем новый, если он отсутствует
        String token = user.getAuthToken();
        if (token == null) {
            token = tokenService.generateToken(user);
        }

        String verificationLink = emailVerificationUrlBase + token;
        authEmailService.sendRegistrationLink(user.getEmail(), verificationLink);
    }

    /**
     * Верифицирует email пользователя по предоставленному токену.
     * Активирует учетную запись пользователя, если токен валиден и не истек.
     *
     * @param token Токен верификации.
     * @throws InvalidAuthToken      если токен не найден или недействителен.
     * @throws TokenExpiredException если срок действия токена истек.
     */
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

    /**
     * Повторно отправляет письмо для верификации email.
     * Генерирует новый токен перед отправкой.
     *
     * @param email Email пользователя для повторной отправки.
     * @throws RuntimeException если пользователь не найден или email уже
     *                          подтвержден.
     */
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