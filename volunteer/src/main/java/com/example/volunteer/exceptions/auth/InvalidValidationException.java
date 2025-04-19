package com.example.volunteer.exceptions.auth;

import lombok.Getter;

import java.util.List;

// Исключение для невалидного пароля

@Getter
public class InvalidValidationException extends RuntimeException {
    private final List<String> errors;

    public InvalidValidationException(List<String> errors) {
        super("Ошибка валидации пароля");
        this.errors = errors;
    }

}