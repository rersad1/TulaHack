package com.example.volunteer.repository;

import com.example.volunteer.model.Task;
import com.example.volunteer.model.TaskCategory;
import com.example.volunteer.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
     List<Task> findByCategoryAndStatusAndAssignedVolunteerIsNotNullAndRatingIsNotNull(
            TaskCategory category, TaskStatus status);

     List<Task> findByUserEmail(String userEmail);
     List<Task> findByStatus(TaskStatus status);
    
}
