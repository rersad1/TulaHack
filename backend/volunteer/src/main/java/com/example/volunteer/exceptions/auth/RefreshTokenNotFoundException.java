package com.example.volunteer.exceptions.auth;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException() {
        super("Refresh token не найден");
    }
}