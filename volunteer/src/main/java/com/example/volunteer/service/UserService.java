package com.example.volunteer.service;

import com.example.volunteer.model.User;
import com.example.volunteer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        if (userRepository.existsById(user.getEmail())) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует.");
        }
        return userRepository.save(user);
    }

    public User updateUser(String email, User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findById(email);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setMiddleName(updatedUser.getMiddleName());
            return userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("Пользователь с таким email не найден.");
        }
    }
}
