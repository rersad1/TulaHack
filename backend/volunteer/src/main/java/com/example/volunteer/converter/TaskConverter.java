package com.example.volunteer.converter;

import com.example.volunteer.DTO.TaskDTO;
import com.example.volunteer.model.Task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskConverter {

    private final UserConverter userConverter;

    public Task dtoToEntity(TaskDTO dto) {
        if (dto == null)
            return null;
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setCategory(dto.getCategory());
        task.setLocationType(dto.getLocationType());
        task.setAddress(dto.getAddress());
        task.setPreferredDateTime(dto.getPreferredDateTime());
        task.setUserEmail(dto.getUserEmail());
        task.setRating(dto.getRating());
        task.setUserComment(dto.getUserComment());
        return task;
    }

    public TaskDTO entityToDto(Task task) {
        if (task == null)
            return null;
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCategory(task.getCategory());
        dto.setLocationType(task.getLocationType());
        dto.setAddress(task.getAddress());
        dto.setPreferredDateTime(task.getPreferredDateTime());
        dto.setUserEmail(task.getUserEmail());
        dto.setRating(task.getRating());
        dto.setUserComment(task.getUserComment());
        if (task.getAssignedVolunteer() != null) {
            dto.setAssignedVolunteer(userConverter.entityToDto(task.getAssignedVolunteer()));
        }
        return dto;
    }
}