package com.example.volunteer.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Double rating;
}