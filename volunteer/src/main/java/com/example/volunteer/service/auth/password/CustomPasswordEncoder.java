package com.example.volunteer.service.auth.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Кастомная реализация PasswordEncoder, использующая BCryptPasswordEncoder.
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Кодирует переданную последовательность символов (пароль).
     * 
     * @param rawPassword Пароль для кодирования.
     * @return Закодированный пароль.
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword);
    }

    /**
     * Сравнивает "сырой" пароль с закодированным.
     * 
     * @param rawPassword     "Сырой" пароль для сравнения.
     * @param encodedPassword Закодированный пароль из хранилища.
     * @return true, если пароли совпадают, иначе false.
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
