package com.example.volunteer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.volunteer.exceptions.auth.*;

// Глобальный обработчик исключений для ошибок с авторизацией

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidValidationException.class)
    public ResponseEntity<?> handleInvalidValidation(InvalidValidationException e) {
        return ResponseEntity.badRequest().body(e.getErrors());
    }
    
    @ExceptionHandler({
        InvalidOldPasswordException.class,
        SamePasswordException.class,
        UserExistsException.class,
        NonExistsUserException.class,
        InvalidAuthToken.class,
        NullUserException.class,
        InvalidPasswordException.class,
    })
    
    public ResponseEntity<?> handleBadRequestExceptions(Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({
        TokenExpiredException.class, 
        RefreshTokenExpiredException.class, 
        RefreshTokenNotFoundException.class 
    })
    public ResponseEntity<?> handleTokenExpiredOrNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}