package com.example.volunteer.service.auth.password;

import org.springframework.stereotype.Component;

import com.example.volunteer.exceptions.auth.InvalidValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * Компонент для проверки пароля на соответствие требованиям безопасности.
 */
@Component
public class PasswordValidator {
    /**
     * Валидирует пароль на соответствие требованиям:
     * - Минимум 8 символов.
     * - Хотя бы одна цифра.
     * - Хотя бы одна строчная буква.
     * - Хотя бы одна заглавная буква.
     * - Хотя бы один специальный символ.
     *
     * @param password Пароль для проверки.
     * @throws InvalidValidationException если пароль не соответствует требованиям,
     *                                    содержит список ошибок.
     */
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