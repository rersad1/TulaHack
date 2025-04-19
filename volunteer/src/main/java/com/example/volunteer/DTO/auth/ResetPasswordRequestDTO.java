package com.example.volunteer.DTO.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Data Transfer Object для запроса сброса пароля.
 */
@Data
public class ResetPasswordRequestDTO {
    /**
     * Адрес электронной почты пользователя, запрашивающего сброс пароля.
     * Должен быть в корректном формате и не может быть пустым.
     */
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;
}
