package com.example.volunteer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.volunteer.exceptions.auth.*;

/**
 * Глобальный обработчик исключений для REST контроллеров.
 * Перехватывает специфичные исключения приложения и возвращает
 * стандартизированные HTTP ответы.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения, связанные с ошибками валидации (например, пароля).
     * Возвращает статус 400 Bad Request и список ошибок.
     * 
     * @param e Исключение InvalidValidationException.
     * @return ResponseEntity с кодом 400 и списком ошибок.
     */
    @ExceptionHandler(InvalidValidationException.class)
    public ResponseEntity<?> handleInvalidValidation(InvalidValidationException e) {
        return ResponseEntity.badRequest().body(e.getErrors());
    }

    /**
     * Обрабатывает общие исключения, связанные с некорректными запросами (например,
     * неверный пароль,
     * пользователь не найден, токен недействителен).
     * Возвращает статус 400 Bad Request и сообщение об ошибке.
     * 
     * @param e Исключение.
     * @return ResponseEntity с кодом 400 и сообщением об ошибке.
     */
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

    /**
     * Обрабатывает исключения, связанные с истекшими или не найденными токенами
     * (одноразовыми и refresh).
     * Возвращает статус 401 Unauthorized и сообщение об ошибке.
     * 
     * @param e Исключение.
     * @return ResponseEntity с кодом 401 и сообщением об ошибке.
     */
    @ExceptionHandler({
            TokenExpiredException.class,
            RefreshTokenExpiredException.class,
            RefreshTokenNotFoundException.class
    })
    public ResponseEntity<?> handleTokenExpiredOrNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}