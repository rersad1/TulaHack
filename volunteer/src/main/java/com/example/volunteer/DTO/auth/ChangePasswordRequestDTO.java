package com.example.volunteer.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequestDTO {
    private String email;

    @NotBlank(message = "Текущий пароль не может быть пустым")
    private String oldPassword;

    @NotBlank(message = "Новый пароль не может быть пустым")
    @Size(min = 8, message = "Новый пароль должен содержать минимум 8 символов")
    private String newPassword;
}