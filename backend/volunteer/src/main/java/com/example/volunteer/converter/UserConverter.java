package com.example.volunteer.converter;

import com.example.volunteer.dto.UserInfoDTO;
import com.example.volunteer.model.auth.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component 
public class UserConverter {

    public UserInfoDTO entityToDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserInfoDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getEnabled()
        );
    }

    public List<UserInfoDTO> entityToDto(List<User> users) {
        return users.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    public User dtoToEntity(UserInfoDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId()); 
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setEnabled(dto.getEnabled());
        return user;
    }
}