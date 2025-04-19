package com.example.volunteer.service.auth.password;

import org.springframework.stereotype.Component;

import com.example.volunteer.exceptions.auth.InvalidValidationException;

import java.util.ArrayList;
import java.util.List;

// Компонент для проверки пароля на соответствие требованиям

@Component

public class PasswordValidator {
    public static void validate(String password) {
        List<String> errors = new ArrayList<>();

        if (password.length() < 8) {
            errors.add("Пароль должен быть не менее 8 символов");
        }
        if (!password.matches(".*\\d.*")) {
            errors.add("Пароль должен содержать хотя бы одну цифру");
        }
        if (!password.matches(".*[a-z].*")) {
            errors.add("Пароль должен содержать хотя бы одну строчную букву");
        }
        if (!password.matches(".*[A-Z].*")) {
            errors.add("Пароль должен содержать хотя бы одну заглавную букву");
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            errors.add("Пароль должен содержать хотя бы один специальный символ");
        }

        if (!errors.isEmpty()) {
            throw new InvalidValidationException(errors);
        }
    }
}