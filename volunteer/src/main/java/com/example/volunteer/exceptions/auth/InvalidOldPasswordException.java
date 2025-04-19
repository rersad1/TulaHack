package com.example.volunteer.exceptions.auth;

public class InvalidOldPasswordException extends RuntimeException {
    public InvalidOldPasswordException() {
        super("Неверный старый пароль");
    }    
}
