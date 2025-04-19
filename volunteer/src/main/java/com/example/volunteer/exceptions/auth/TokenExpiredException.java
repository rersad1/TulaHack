package com.example.volunteer.exceptions.auth;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException() {
        super("Токен устарел");
    }
    
}
