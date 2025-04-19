package com.example.volunteer.DTO.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object для входа с использованием токена (например, из
 * подтверждения email или сброса пароля).
 */
@Getter
@Setter
public class LoginTokenRequestDTO {
    /**
     * Токен, используемый для аутентификации.
     */
    private String token;
}