package com.example.volunteer.service.auth.jwtToken;

import com.example.volunteer.model.auth.JwtRefreshToken;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.JwtRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для управления JWT refresh токенами.
 * Отвечает за создание, поиск, проверку срока действия и удаление refresh
 * токенов.
 */
@Service
public class JwtRefreshTokenService {

    private static final long REFRESH_TOKEN_EXPIRATION_DAYS = 7; // Срок действия refresh токена в днях

    @Autowired
    private JwtRefreshTokenRepository refreshTokenRepository;

    /**
     * Создает новый JWT refresh токен для пользователя.
     * Удаляет предыдущий refresh токен пользователя, если он существует.
     *
     * @param user Пользователь, для которого создается токен.
     * @return Созданный и сохраненный JwtRefreshToken.
     */

    @Transactional 
    public JwtRefreshToken createRefreshToken(User user) {
        // Удаляем существующий токен для этого пользователя, чтобы гарантировать
        // только один активный refresh токен на пользователя.
        refreshTokenRepository.deleteByUser(user);

        // Создаем новый объект refresh токена
        JwtRefreshToken refreshToken = new JwtRefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString()); // Генерируем уникальный токен
        refreshToken.setUser(user); // Связываем с пользователем
        // Устанавливаем срок действия (текущее время + N дней)
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(REFRESH_TOKEN_EXPIRATION_DAYS));

        // Сохраняем новый токен в базе данных и возвращаем его
        return refreshTokenRepository.save(refreshToken);
    }


    /**
     * Находит JWT refresh токен по его строковому значению.
     *
     * @param token Строковое значение токена.
     * @return Optional, содержащий найденный токен, или пустой Optional.
     */
    public Optional<JwtRefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Проверяет, истек ли срок действия JWT refresh токена.
     *
     * @param token Токен для проверки.
     * @return true, если срок действия токена истек, иначе false.
     */
    public boolean isExpired(JwtRefreshToken token) {
        return token.getExpiryDate().isBefore(LocalDateTime.now());
    }

    /**
     * Удаляет JWT refresh токен из репозитория.
     *
     * @param token Токен для удаления.
     */
    public void delete(JwtRefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    /**
     * Удаляет JWT refresh токен по его ID.
     *
     * @param id ID токена для удаления.
     */
    public void deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
    }

    


}