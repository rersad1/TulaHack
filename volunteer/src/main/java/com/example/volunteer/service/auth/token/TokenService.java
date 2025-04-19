package com.example.volunteer.service.auth.token;

import com.example.volunteer.exceptions.auth.InvalidAuthToken;
import com.example.volunteer.exceptions.auth.TokenExpiredException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сервис для управления одноразовыми токенами аутентификации (не JWT).
 * Используется для верификации email, сброса пароля и подтверждения входа.
 */
@Service
public class TokenService {

    private static final long TOKEN_EXPIRATION_MINUTES = 15; // Срок действия токена в минутах

    @Autowired
    private UserRepository userRepository;

    /**
     * Генерирует новый одноразовый токен для пользователя и сохраняет его.
     *
     * @param user Пользователь, для которого генерируется токен.
     * @return Сгенерированный токен.
     */
    public String generateToken(User user) {
        String token = UUID.randomUUID().toString();

        user.setAuthToken(token);
        user.setTokenCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        return token;
    }

    /**
     * Валидирует предоставленный токен.
     * Проверяет существование пользователя с таким токеном и срок действия токена.
     *
     * @param token Токен для валидации.
     * @return Пользователь, связанный с токеном.
     * @throws InvalidAuthToken      если токен не найден или недействителен.
     * @throws TokenExpiredException если срок действия токена истек.
     */
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

    /**
     * Очищает (удаляет) токен и время его создания у пользователя.
     *
     * @param user Пользователь, у которого нужно очистить токен.
     */
    public void clearToken(User user) {
        user.setAuthToken(null);
        user.setTokenCreatedAt(null);
        userRepository.save(user);
    }

    /**
     * Проверяет, истек ли срок действия токена.
     *
     * @param tokenCreatedAt Время создания токена.
     * @return true, если токен истек или время создания не задано, иначе false.
     */
    public boolean isTokenExpired(LocalDateTime tokenCreatedAt) {
        if (tokenCreatedAt == null) {
            return true;
        }

        LocalDateTime expirationTime = tokenCreatedAt.plusMinutes(TOKEN_EXPIRATION_MINUTES);
        return LocalDateTime.now().isAfter(expirationTime);
    }
}