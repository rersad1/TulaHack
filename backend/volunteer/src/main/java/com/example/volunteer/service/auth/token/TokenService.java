package com.example.volunteer.service.auth.token;

import com.example.volunteer.exceptions.auth.InvalidAuthToken;
import com.example.volunteer.exceptions.auth.TokenExpiredException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant; // Используем Instant
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    private static final long TOKEN_VALIDITY_MINUTES = 15;
    private static final SecureRandom random = new SecureRandom();

    @Autowired
    private UserRepository userRepository;

    /**
     * Генерирует уникальный 6-значный числовой токен, связывает его с пользователем,
     * устанавливает временную метку создания (Instant), сохраняет пользователя и возвращает токен.
     * Проверяет на коллизии и генерирует заново, если токен уже существует.
     *
     * @param user Пользователь, для которого нужно сгенерировать токен.
     * @return Сгенерированный 6-значный токен в виде строки.
     */
    public String generateToken(User user) {
        String token;
        do {
            int number = 100000 + random.nextInt(900000);
            token = String.valueOf(number);
        } while (userRepository.existsByAuthToken(token));

        user.setAuthToken(token);
        user.setTokenCreatedAt(Instant.now());
        userRepository.save(user);
        return token;
    }

    /**
     * Очищает токен аутентификации и его временную метку создания для пользователя.
     *
     * @param user Пользователь, чей токен должен быть очищен.
     */
    public void clearToken(User user) {
        user.setAuthToken(null);
        user.setTokenCreatedAt(null);
        userRepository.save(user);
    }

    /**
     * Проверяет, истек ли срок действия токена, связанного с данным временем создания.
     *
     * @param tokenCreationInstant Время создания токена (Instant).
     * @return true, если токен истек или время создания равно null, иначе false.
     */
    public boolean isTokenExpired(Instant tokenCreationInstant) {
        if (tokenCreationInstant == null) {
            return true; // Считаем токен без времени создания истекшим/невалидным
        }
        // Сравниваем моменты времени Instant
        return tokenCreationInstant.plus(TOKEN_VALIDITY_MINUTES, ChronoUnit.MINUTES).isBefore(Instant.now());
    }

    /**
     * Валидирует предоставленный одноразовый токен.
     * Находит пользователя по токену и проверяет срок его действия (используя Instant).
     *
     * @param token Одноразовый токен для валидации.
     * @return Объект User, связанный с валидным токеном.
     * @throws InvalidAuthToken если токен не найден.
     * @throws TokenExpiredException если срок действия токена истек.
     */
    public User validateToken(String token) {
        token = token.replace("\"", "");

        User user = userRepository.findByAuthToken(token).orElseThrow(InvalidAuthToken::new);

        // Проверка срока действия токена с использованием Instant
        if (isTokenExpired(user.getTokenCreatedAt())) {
            clearToken(user);
            throw new TokenExpiredException();
        }

        return user;
    }
}