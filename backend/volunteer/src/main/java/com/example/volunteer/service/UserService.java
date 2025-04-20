package com.example.volunteer.service;

import com.example.volunteer.converter.UserConverter;
import com.example.volunteer.dto.UserInfoDTO;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Transactional(readOnly = true) 
    public List<UserInfoDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return userConverter.entityToDto(users);
    }

    @Transactional(readOnly = true)
    public Optional<UserInfoDTO> getUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.map(userConverter::entityToDto);
    }


}