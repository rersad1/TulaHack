package com.example.volunteer.DTO;

import com.example.volunteer.model.auth.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerStatsDto {
    private User volunteer;
    private double averageRating;
    private long completedTasksCount;
}