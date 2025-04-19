package com.example.volunteer.exceptions.auth;

public class InvalidAuthToken extends RuntimeException {
    public InvalidAuthToken() {
        super("Недействительный токен");
    }
}
