package com.example.volunteer.service.auth.token;

import com.example.volunteer.exceptions.auth.InvalidAuthToken;
import com.example.volunteer.exceptions.auth.TokenExpiredException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
        // Используем Instant.now() для получения текущего момента времени в UTC
        user.setTokenCreatedAt(Instant.now());
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
     * @param tokenCreatedAt Время создания токена (тип Instant).
     * @return true, если токен истек или время создания не задано, иначе false.
     */
    // Принимаем Instant в качестве параметра
    public boolean isTokenExpired(Instant tokenCreatedAt) { // <<< Изменен тип параметра
        if (tokenCreatedAt == null) {
            return true;
        }

        // Рассчитываем время истечения, добавляя минуты к Instant
        Instant expirationTime = tokenCreatedAt.plus(TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES); // <<< Изменено
        // Сравниваем текущий момент времени (UTC) с временем истечения
        return Instant.now().isAfter(expirationTime); // <<< Изменено
    }
}