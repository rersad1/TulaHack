package com.example.volunteer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean enabled;
}