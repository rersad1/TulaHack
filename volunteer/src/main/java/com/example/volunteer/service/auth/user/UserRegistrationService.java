package com.example.volunteer.service.auth.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.volunteer.exceptions.auth.NullUserException;
import com.example.volunteer.exceptions.auth.UserExistsException;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import com.example.volunteer.service.auth.password.PasswordValidator;
import com.example.volunteer.service.auth.token.TokenService;
import com.example.volunteer.DTO.auth.UserRegistrationDTO;

@Service
public class UserRegistrationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailVerificationService emailVerificationService;

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
        
        tokenService.generateToken(user);

        User savedUser = userRepository.save(user);;

        emailVerificationService.sendVerificationEmail(savedUser);

        return savedUser;
    }
}