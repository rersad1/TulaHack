package com.example.volunteer.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO {
    private Long id;
    private String description;
    private String email;
    private Double averageRating;
}