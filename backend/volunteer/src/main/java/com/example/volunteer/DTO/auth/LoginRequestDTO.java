package com.example.volunteer.DTO.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object (DTO) для запроса входа пользователя.
 * Содержит учетные данные, необходимые для аутентификации.
 */
@Data
public class LoginRequestDTO {

    /**
     * Email пользователя. Должен быть не пустым и в корректном формате email.
     */
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    /**
     * Пароль пользователя. Должен быть не пустым.
     */
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;
}