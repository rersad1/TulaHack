package com.example.volunteer.service;

import com.example.volunteer.DTO.ReviewDTO;
import com.example.volunteer.DTO.TaskDTO;
import com.example.volunteer.model.Task;
import com.example.volunteer.model.TaskStatus;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.TaskRepository;
import com.example.volunteer.repository.auth.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public Task create(TaskDTO taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setStatus(TaskStatus.OPEN);
        task.setCategory(taskDto.getCategory());
        task.setUserEmail(taskDto.getUserEmail());
        return taskRepository.save(task);
    }

    @Transactional
    public Optional<Task> update(Long id, TaskDTO taskDto) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(taskDto.getTitle());
            task.setDescription(taskDto.getDescription());
            task.setStatus(taskDto.getStatus());
            task.setCategory(taskDto.getCategory());

            return taskRepository.save(task);
        });
    }

    @Transactional(readOnly = true)
    public Optional<Task> getTask(Long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Optional<Task> respondToTask(Long taskId, String userId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        Optional<User> userOptional = userRepository.findById(userId);

        if (taskOptional.isPresent() && userOptional.isPresent()) {
            Task task = taskOptional.get();
            User user = userOptional.get();

            if (task.getStatus() == TaskStatus.OPEN && "VOLUNTEER".equals(user.getRole())) {
                task.getResponders().add(user);
                task.setAssignedVolunteer(user);
                task.setStatus(TaskStatus.IN_PROGRESS);
                return Optional.of(taskRepository.save(task));
            }
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Optional<Set<User>> getResponders(Long taskId) {
        return taskRepository.findById(taskId).map(Task::getResponders);
    }

    @Transactional
    public Optional<Task> addReview(Long taskId, ReviewDTO reviewDto) {
        return taskRepository.findById(taskId).map(task -> {

            if (task.getStatus() == TaskStatus.COMPLETED) {
                task.setRating(reviewDto.getRating());
                task.setUserComment(reviewDto.getUserComment());
                return taskRepository.save(task);
            }

            return task;
        });
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByUserEmail(String userEmail) {
        return taskRepository.findByUserEmail(userEmail);
    }
}