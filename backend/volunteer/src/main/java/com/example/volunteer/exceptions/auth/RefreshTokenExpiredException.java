package com.example.volunteer.exceptions.auth;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() {
        super("Refresh token истек");
    }
    
}
