package com.example.volunteer.controller;

import com.example.volunteer.DTO.UserInfoDTO;
import com.example.volunteer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") 
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/v1/users")
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() {
        List<UserInfoDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/v1/users/{id}")
    public ResponseEntity<UserInfoDTO> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build()); 
    }

    /**
     * Получает данные профиля текущего аутентифицированного пользователя.
     * @param userDetails Детали аутентифицированного пользователя (внедряются Spring Security).
     * @return ResponseEntity с UserInfoDTO или notFound, если пользователь не найден.
     */
    @GetMapping("/profile")
    public ResponseEntity<UserInfoDTO> getCurrentUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build(); 
        }
        String userEmail = userDetails.getUsername(); 
        return userService.getUserByEmail(userEmail)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}