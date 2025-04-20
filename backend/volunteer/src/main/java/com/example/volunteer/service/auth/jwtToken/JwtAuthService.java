package com.example.volunteer.service.auth.jwtToken;

import com.example.volunteer.DTO.auth.LoginResponseDTO;
import com.example.volunteer.exceptions.auth.RefreshTokenExpiredException;
import com.example.volunteer.exceptions.auth.RefreshTokenNotFoundException;
import com.example.volunteer.model.auth.JwtRefreshToken;
import com.example.volunteer.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для аутентификации с использованием JWT.
 * Отвечает за выдачу и обновление пар access и refresh токенов.
 */
@Service
public class JwtAuthService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtRefreshTokenService jwtRefreshTokenService;

    /**
     * Выдает новую пару JWT access и refresh токенов для пользователя.
     *
     * @param user Пользователь, для которого выдаются токены.
     * @return LoginResponseDTO, содержащий access и refresh токены.
     */
    public LoginResponseDTO issueTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtRefreshTokenService.createRefreshToken(user).getToken();
        String userRole = user.getRole(); // Получаем роль пользователя

        return new LoginResponseDTO(accessToken, refreshToken, userRole);
    }

    /**
     * Обновляет пару JWT токенов, используя предоставленный refresh токен.
     *
     * @param refreshToken Строковое значение refresh токена.
     * @return LoginResponseDTO, содержащий новые access и refresh токены.
     * @throws RefreshTokenNotFoundException если refresh токен не найден.
     * @throws RefreshTokenExpiredException  если refresh токен истек.
     */
    public LoginResponseDTO refreshToken(String refreshToken) {
        JwtRefreshToken foundToken = jwtRefreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new RefreshTokenNotFoundException());

        if (jwtRefreshTokenService.isExpired(foundToken)) {
            jwtRefreshTokenService.deleteById(foundToken.getId()); // Удаляем истекший токен
            throw new RefreshTokenExpiredException();
        }

        User user = foundToken.getUser();
        // Метод issueTokens теперь будет генерировать правильный access токен
        return issueTokens(user);
    }
}