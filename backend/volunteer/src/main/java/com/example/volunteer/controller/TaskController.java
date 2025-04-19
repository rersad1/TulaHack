package com.example.volunteer.controller;

import com.example.volunteer.converter.TaskConverter;
import com.example.volunteer.DTO.TaskDTO;
import com.example.volunteer.DTO.ReviewDTO;
import com.example.volunteer.model.Task;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto) {
        Task createdTask = taskService.create(taskDto);
        return ResponseEntity.ok(TaskConverter.entityToDto(createdTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id,
                                              @RequestBody TaskDTO taskDto) {
        return taskService.update(id, taskDto)
                .map(TaskConverter::entityToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint для получения информации о задаче
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        return taskService.getTask(id)
                .map(TaskConverter::entityToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint для отклика на заявку 
    @PostMapping("/{id}/respond")
    public ResponseEntity<?> respondToTask(@PathVariable Long id,
                                           @RequestParam String userId) {
        return taskService.respondToTask(id, userId)
                .map(task -> ResponseEntity.ok().build())
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint для получения пользователей, откликнувшихся на задачу
    @GetMapping("/{id}/responders")
    public ResponseEntity<Set<User>> getResponders(@PathVariable Long id) {
        return taskService.getResponders(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Endpoint для добавления отзыва и комментария от автора заявки
    @PostMapping("/{id}/review")
    public ResponseEntity<TaskDTO> addReview(@PathVariable Long id,
                                             @RequestBody ReviewDTO reviewDto) {
        return taskService.addReview(id, reviewDto)
                .map(TaskConverter::entityToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}