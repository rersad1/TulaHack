package com.example.volunteer.service;

import com.example.volunteer.model.Task;
import com.example.volunteer.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task create(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> update(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(existing -> {
            existing.setTitle(updatedTask.getTitle());
            existing.setDescription(updatedTask.getDescription());
            existing.setStatus(updatedTask.getStatus());
            existing.setCategory(updatedTask.getCategory());
            existing.setUserEmail(updatedTask.getUserEmail());
            existing.setRating(updatedTask.getRating());
            existing.setUserComment(updatedTask.getUserComment());
            return taskRepository.save(existing);
        });
    }
}
