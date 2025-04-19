package com.example.volunteer.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object для смены пароля пользователя.
 */
@Data
public class ChangePasswordRequestDTO {
    /**
     * Email пользователя, меняющего пароль. Может устанавливаться внутренне
     * сервисом.
     */
    private String email;

    /**
     * Текущий пароль пользователя. Не может быть пустым.
     */
    @NotBlank(message = "Текущий пароль не может быть пустым")
    private String oldPassword;

    /**
     * Желаемый новый пароль. Должен содержать минимум 8 символов и не может быть
     * пустым.
     */
    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, message = "Новый пароль должен содержать минимум 8 символов")
    private String newPassword;
}