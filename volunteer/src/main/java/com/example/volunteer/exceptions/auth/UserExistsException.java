package com.example.volunteer.exceptions.auth;

public class UserExistsException extends RuntimeException {
    public UserExistsException() {
        super("Пользователь с таким email уже существует");
    }
}
