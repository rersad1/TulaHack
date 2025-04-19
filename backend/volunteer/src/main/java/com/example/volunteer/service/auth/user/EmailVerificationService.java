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
        // Используем существующий токен или создаем новый, если он отсутствует
        String token = user.getAuthToken();
        if (token == null || tokenService.isTokenExpired(user.getTokenCreatedAt())) { // Проверяем и на истечение срока
            token = tokenService.generateToken(user); // Генерируем новый, если старый истек или отсутствует
        }

        // Формируем ссылку на фронтенд
        String verificationLink = frontendBaseUrl + frontendVerificationPath + "/" + token;
        authEmailService.sendRegistrationLink(user.getEmail(), verificationLink);
    }

    /**
     * Верифицирует email пользователя по предоставленному токену.
     * Активирует учетную запись пользователя, если токен валиден и не истек.
     * Этот метод вызывается API контроллером, когда фронтенд отправляет токен.
     *
     * @param token Токен верификации.
     * @throws InvalidAuthToken      если токен не найден или недействителен.
     * @throws TokenExpiredException если срок действия токена истек.
     */
    public void verifyEmail(String token) {
        // Удаляем возможные кавычки, если они передаются
        token = token.replace("\"", "");

        User user = userRepository.findByAuthToken(token).orElseThrow(InvalidAuthToken::new);

        // Проверка срока действия токена
        if (tokenService.isTokenExpired(user.getTokenCreatedAt())) {
            // Можно не удалять токен сразу, а дать возможность переотправить письмо
            // tokenService.clearToken(user);
            throw new TokenExpiredException();
        }

        // Активируем учетную запись пользователя и очищаем токен
        user.setEnabled(true);
        tokenService.clearToken(user); // Очищаем токен после успешной верификации
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден")); // Используйте более специфичное
                                                                                    // исключение, если нужно

        if (Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("Email уже подтвержден"); // Используйте более специфичное исключение
        }

        // Генерируем новый токен (старый будет перезаписан)
        tokenService.generateToken(user);

        sendVerificationEmail(user); // Отправляем письмо с новым токеном и ссылкой на фронтенд
    }
}