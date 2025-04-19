package com.example.volunteer.DTO.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object для ответа при входе. Содержит токен доступа и токен
 * обновления.
 */
@Data
@AllArgsConstructor
public class LoginResponseDTO {
    /**
     * Токен доступа для аутентификации последующих запросов.
     */
    private String accessToken;
    /**
     * Токен обновления, используемый для получения нового токена доступа.
     */
    private String refreshToken;
}