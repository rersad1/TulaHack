package com.example.volunteer.service.auth.user;

import com.example.volunteer.exceptions.auth.InvalidPasswordException;
import com.example.volunteer.exceptions.auth.NonExistsUserException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import com.example.volunteer.service.auth.password.CustomPasswordEncoder;
import com.example.volunteer.service.auth.token.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки входа пользователя в систему.
 */
@Service
public class UserLoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomPasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    /**
     * Выполняет вход пользователя по email и паролю.
     * Проверяет существование пользователя и корректность пароля.
     * Генерирует и возвращает одноразовый токен для подтверждения входа через
     * email.
     *
     * @param email    Email пользователя.
     * @param password Пароль пользователя.
     * @return Сгенерированный одноразовый токен для подтверждения входа.
     * @throws NonExistsUserException   если пользователь с таким email не найден.
     * @throws InvalidPasswordException если введенный пароль некорректен.
     */
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
