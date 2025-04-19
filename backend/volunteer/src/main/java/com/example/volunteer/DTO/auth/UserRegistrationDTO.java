package com.example.volunteer.DTO.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object для регистрации пользователя.
 */
@Data
public class UserRegistrationDTO {

    /**
     * Адрес электронной почты пользователя. Должен быть в корректном формате и не
     * может быть пустым.
     */
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    /**
     * Пароль пользователя. Должен содержать минимум 8 символов и не может быть
     * пустым.
     */
    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    /**
     * Имя пользователя. Не может быть пустым, максимальная длина 50 символов.
     */
    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 50, message = "Имя не может быть длиннее 50 символов")
    private String firstName;

    /**
     * Фамилия пользователя. Не может быть пустой, максимальная длина 50 символов.
     */
    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 50, message = "Фамилия не может быть длиннее 50 символов")
    private String lastName;

    /**
     * Роль пользователя. Должна быть "USER" или "VOLUNTEER" и не может быть пустой.
     */
    @NotBlank(message = "Роль не может быть пустой")
    @Pattern(regexp = "^(USER|VOLUNTEER)$", message = "Допустимые роли: USER, VOLUNTEER")
    private String role;
}