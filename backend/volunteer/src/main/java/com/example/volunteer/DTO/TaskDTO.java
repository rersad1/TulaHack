package com.example.volunteer.DTO;

import java.time.LocalDateTime;

import com.example.volunteer.model.LocationType;
import com.example.volunteer.model.TaskCategory;
import com.example.volunteer.model.TaskStatus;
import com.example.volunteer.DTO.UserInfoDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskCategory category;
    private String userEmail;
    private Integer rating;
    private String userComment;
    private LocationType locationType;
    private String address;
    private LocalDateTime preferredDateTime;
    private UserInfoDTO assignedVolunteer;
}