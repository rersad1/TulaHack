package com.example.volunteer.service.auth.user;

import com.example.volunteer.exceptions.auth.InvalidOldPasswordException;
import com.example.volunteer.exceptions.auth.NonExistsUserException;
import com.example.volunteer.exceptions.auth.SamePasswordException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import com.example.volunteer.service.auth.password.PasswordValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Сервис для смены пароля из настроек пользователя

@Service
public class UserChangePasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NonExistsUserException());

        // Проверка на корректость старого пароля
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new InvalidOldPasswordException();
        }

        // Проверка на совпадение нового пароля со старым
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new SamePasswordException();
        }

        // Проверка на корректность нового пароля
        PasswordValidator.validate(newPassword);

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);
        userRepository.save(user);
    }
}