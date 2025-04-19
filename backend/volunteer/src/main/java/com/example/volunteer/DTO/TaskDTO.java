package com.example.volunteer.DTO;

import com.example.volunteer.model.TaskCategory;
import com.example.volunteer.model.TaskStatus;
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
}