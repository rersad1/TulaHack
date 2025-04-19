package com.example.volunteer.exceptions.auth;

public class SamePasswordException extends RuntimeException {
    public SamePasswordException() {
        super("Новый пароль совпадает со старым");
    }
}
