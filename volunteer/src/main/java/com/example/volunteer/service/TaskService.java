package com.example.volunteer.service;

import com.example.volunteer.converter.TaskConverter;
import com.example.volunteer.DTO.TaskDTO;
import com.example.volunteer.model.Task;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.TaskRepository;
import com.example.volunteer.repository.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public Task create(TaskDTO dto) {
        Task task = TaskConverter.dtoToEntity(dto);
        return taskRepository.save(task);
    }

    public Optional<Task> update(Long id, TaskDTO dto) {
        return taskRepository.findById(id).map(existing -> {
            Task updated = TaskConverter.dtoToEntity(dto);
            existing.setTitle(updated.getTitle());
            existing.setDescription(updated.getDescription());
            existing.setStatus(updated.getStatus());
            existing.setCategory(updated.getCategory());
            existing.setUserEmail(updated.getUserEmail());
            existing.setRating(updated.getRating());
            existing.setUserComment(updated.getUserComment());
            return taskRepository.save(existing);
        });
    }

    public Optional<Task> respondToTask(Long taskId, String userId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<User> userOpt = userRepository.findById(userId);
        if (taskOpt.isPresent() && userOpt.isPresent()) {
            Task task = taskOpt.get();

            task.getResponders().add(userOpt.get());
            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }

    public Optional<Set<User>> getResponders(Long taskId) {
        return taskRepository.findById(taskId).map(Task::getResponders);
    }
}