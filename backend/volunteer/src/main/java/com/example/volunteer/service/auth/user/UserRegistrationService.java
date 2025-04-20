package com.example.volunteer.service.auth.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.volunteer.exceptions.auth.InvalidValidationException;
import com.example.volunteer.exceptions.auth.NullUserException;
import com.example.volunteer.exceptions.auth.UserExistsException;
import com.example.volunteer.model.Volunteer;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import com.example.volunteer.repository.VolunteerRepository;
import com.example.volunteer.service.auth.password.PasswordValidator;
import com.example.volunteer.service.auth.token.TokenService;
import com.example.volunteer.DTO.auth.UserRegistrationDTO;

/**
 * Сервис для регистрации новых пользователей.
 */
@Service
public class UserRegistrationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VolunteerRepository volunteerRepository; // Добавить поле и аннотацию

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    /**
     * Регистрирует нового пользователя в системе.
     * Проверяет существование пользователя, валидирует пароль, сохраняет
     * пользователя
     * и отправляет email для верификации.
     *
     * @param registrationDTO DTO с данными для регистрации.
     * @return Сохраненный объект User.
     * @throws NullUserException          если registrationDTO равен null.
     * @throws UserExistsException        если пользователь с таким email уже
     *                                    существует.
     * @throws InvalidValidationException если пароль не соответствует требованиям
     *                                    безопасности.
     */
    @Transactional
    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (registrationDTO == null) {
            throw new NullUserException();
        }

        String email = registrationDTO.getEmail();
        String password = registrationDTO.getPassword();
        String firstName = registrationDTO.getFirstName();
        String lastName = registrationDTO.getLastName();
        String role = registrationDTO.getRole();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserExistsException();
        }

        PasswordValidator.validate(password);

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(role);

        // Генерируем ID для пользователя
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(false);

        tokenService.generateToken(user); // Токен теперь генерируется в EmailVerificationService

        User savedUser = userRepository.save(user);

        if ("ROLE_VOLUNTEER".equals(savedUser.getRole())) {
            Volunteer volunteerProfile = new Volunteer(savedUser);
            // Установить organizationId и middleName, если они есть в DTO и нужны
            // volunteerProfile.setOrganizationId(registrationDTO.getOrganizationId()); // Пример
            // volunteerProfile.setMiddleName(registrationDTO.getMiddleName()); // Пример
            volunteerRepository.save(volunteerProfile);
        }

        emailVerificationService.sendVerificationEmail(savedUser); // Отправляем письмо после всех сохранений

        return savedUser;
    }
}