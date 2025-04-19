package com.example.volunteer.controller;

import com.example.volunteer.DTO.UserDto;
import com.example.volunteer.model.TaskCategory;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/volunteers")
    public ResponseEntity<List<UserDto>> getRecommendedVolunteers(
            @RequestParam("category") TaskCategory category) { 

        List<User> recommendedVolunteers = recommendationService.recommendVolunteers(category);

        
        List<UserDto> recommendedVolunteerDtos = recommendedVolunteers.stream()
                .map(user -> new UserDto(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(recommendedVolunteerDtos);
    }
}