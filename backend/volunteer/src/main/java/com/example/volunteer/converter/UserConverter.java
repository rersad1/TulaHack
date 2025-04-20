package com.example.volunteer.converter;

import com.example.volunteer.DTO.UserInfoDTO;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.model.Volunteer;
import com.example.volunteer.repository.VolunteerRepository;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {

    @Autowired
    private VolunteerRepository volunteerRepository;

    public UserInfoDTO entityToDto(User user) {
        if (user == null) {
            return null;
        }
        Double rating = null;
        
        if ("VOLUNTEER".equals(user.getRole())) {
            rating = volunteerRepository.findById(user.getId()) 
                                        .map(Volunteer::getRating)
                                        .orElse(null);
        }

        return new UserInfoDTO(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getEnabled(),
                rating 
        );
    }


    public List<UserInfoDTO> entityToDto(List<User> users) {
        return users.stream()
                .map(this::entityToDto) 
                .collect(Collectors.toList());
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