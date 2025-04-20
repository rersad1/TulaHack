package com.example.volunteer.controller;

import com.example.volunteer.converter.TaskConverter;
import com.example.volunteer.converter.UserConverter; 
import com.example.volunteer.DTO.TaskDTO;
import com.example.volunteer.DTO.UserDto; 
import com.example.volunteer.DTO.UserInfoDTO; 
import com.example.volunteer.DTO.ReviewDTO;
import com.example.volunteer.model.Task;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository; 
import com.example.volunteer.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskConverter taskConverter;
    private final UserConverter userConverter;
    private final UserRepository userRepository; 

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        taskDto.setUserEmail(userEmail);

        Task createdTask = taskService.create(taskDto);
        return ResponseEntity.ok(taskConverter.entityToDto(createdTask));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id,
                                              @RequestBody TaskDTO taskDto) {

        return taskService.update(id, taskDto)
                .map(task -> taskConverter.entityToDto(task)) 
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        return taskService.getTask(id)
                .map(task -> taskConverter.entityToDto(task)) 
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<List<TaskDTO>> getMyTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userEmail = authentication.getName();

        List<Task> tasks = taskService.getTasksByUserEmail(userEmail);
        List<TaskDTO> taskDtos = tasks.stream()
                                      .map(task -> taskConverter.entityToDto(task)) 
                                      .collect(Collectors.toList());
        return ResponseEntity.ok(taskDtos);
    }


   @PostMapping("/{id}/respond")
    public ResponseEntity<?> respondToTask(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = auth.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Optional<Task> updated = taskService.respondToTask(id, userOpt.get().getId());
        if (updated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Could not respond to task. Check status or role.");
        }
        TaskDTO dto = taskConverter.entityToDto(updated.get());
        return ResponseEntity.ok(dto);
    }


    @GetMapping("/{id}/responders")
    public ResponseEntity<Set<UserInfoDTO>> getResponders(@PathVariable Long id) { 
        return taskService.getResponders(id)
                .map(users -> users.stream()
                                   .map(user -> userConverter.entityToDto(user))
                                   .collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }




    @PostMapping("/{id}/review")
    public ResponseEntity<?> addReview(@PathVariable Long id,
                                       @RequestBody ReviewDTO reviewDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = auth.getName();
        Optional<Task> taskOpt = taskService.getTask(id);
        if (taskOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!taskOpt.get().getUserEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only the task creator can add a review.");
        }
        Optional<Task> updated = taskService.addReview(id, reviewDto);
        if (updated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Review could not be added. Task might not be completed.");
        }
        TaskDTO dto = taskConverter.entityToDto(updated.get());
        return ResponseEntity.ok(dto);
    }
}