package com.example.volunteer.service.auth.user;

import com.example.volunteer.exceptions.auth.InvalidPasswordException;
import com.example.volunteer.exceptions.auth.NonExistsUserException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import com.example.volunteer.service.auth.password.CustomPasswordEncoder;
import com.example.volunteer.service.auth.token.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomPasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    public String login(String email, String password) {
        
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NonExistsUserException());

        // Проверка на корректность пароля
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException();
        }

        // Сразу генерируем и возвращаем токен
        return tokenService.generateToken(user);
    }

}
