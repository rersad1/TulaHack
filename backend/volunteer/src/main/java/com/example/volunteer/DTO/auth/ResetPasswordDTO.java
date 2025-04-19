package com.example.volunteer.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object для сброса пароля с использованием токена.
 */
@Data
public class ResetPasswordDTO {
    /**
     * Токен сброса пароля, полученный пользователем. Не может быть пустым.
     */
    @NotBlank(message = "Токен не может быть пустым")
    private String token;

    /**
     * Новый пароль пользователя. Должен содержать минимум 8 символов и не может
     * быть пустым.
     */
    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String newPassword;
}
