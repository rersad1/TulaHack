package com.example.volunteer.exceptions.auth;

public class InvalidPasswordException extends RuntimeException {
    public InvalidPasswordException() {
        super("Неккоректный пароль");
    }
}
