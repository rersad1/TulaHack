package com.example.volunteer.DTO.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный формат email")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Пароль должен содержать минимум 8 символов")
    private String password;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 50, message = "Имя не может быть длиннее 50 символов")
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(max = 50, message = "Фамилия не может быть длиннее 50 символов")
    private String lastName;

    @NotBlank(message = "Роль не может быть пустой")
    @Pattern(regexp = "^(USER|VOLUNTEER)$", message = "Допустимые роли: USER, VOLUNTEER")
    private String role;
}