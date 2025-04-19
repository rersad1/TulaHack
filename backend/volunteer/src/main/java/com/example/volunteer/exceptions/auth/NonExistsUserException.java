package com.example.volunteer.exceptions.auth;

public class NonExistsUserException extends RuntimeException {
    public NonExistsUserException() {
        super("Пользователь с таким email не существует");
    }
}
