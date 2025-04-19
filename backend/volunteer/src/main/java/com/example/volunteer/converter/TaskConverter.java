package com.example.volunteer.converter;

import com.example.volunteer.DTO.TaskDTO;
import com.example.volunteer.model.Task;

public class TaskConverter {

    public static Task dtoToEntity(TaskDTO dto) {
        if(dto == null) return null;
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setCategory(dto.getCategory());
        task.setUserEmail(dto.getUserEmail());
        task.setRating(dto.getRating());
        task.setUserComment(dto.getUserComment());
        return task;
    }

    public static TaskDTO entityToDto(Task task) {
        if(task == null) return null;
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCategory(task.getCategory());
        dto.setUserEmail(task.getUserEmail());
        dto.setRating(task.getRating());
        dto.setUserComment(task.getUserComment());
        return dto;
    }
}