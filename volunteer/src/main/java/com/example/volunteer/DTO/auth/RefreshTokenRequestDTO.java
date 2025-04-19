package com.example.volunteer.DTO.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object для запроса нового токена доступа с использованием
 * токена обновления.
 */
@Getter
@Setter
public class RefreshTokenRequestDTO {
    /**
     * Токен обновления, используемый для получения нового токена доступа.
     */
    private String refreshToken;

}