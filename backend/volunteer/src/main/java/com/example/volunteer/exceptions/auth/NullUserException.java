package com.example.volunteer.exceptions.auth;

public class NullUserException extends RuntimeException {
    public NullUserException() {
        super("User не может быть null");
    }
}
