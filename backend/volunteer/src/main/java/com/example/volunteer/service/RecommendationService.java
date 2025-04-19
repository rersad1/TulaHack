package com.example.volunteer.service;

import com.example.volunteer.DTO.VolunteerStatsDto;
import com.example.volunteer.model.Task;
import com.example.volunteer.model.TaskCategory;
import com.example.volunteer.model.TaskStatus;
import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.TaskRepository;
import com.example.volunteer.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository; 



    public List<User> recommendVolunteers(TaskCategory category) {
        
        List<Task> completedTasks = taskRepository.findByCategoryAndStatusAndAssignedVolunteerIsNotNullAndRatingIsNotNull(
                category, TaskStatus.COMPLETED);

        if (completedTasks.isEmpty()) {
            return Collections.emptyList();
        }

        
        Map<User, List<Task>> tasksByVolunteer = completedTasks.stream()
                .filter(task -> task.getAssignedVolunteer() != null && "VOLUNTEER".equals(task.getAssignedVolunteer().getRole())) 
                .collect(Collectors.groupingBy(Task::getAssignedVolunteer));

        List<VolunteerStatsDto> volunteerStatsList = new ArrayList<>(); 
        for (Map.Entry<User, List<Task>> entry : tasksByVolunteer.entrySet()) {
            User volunteer = entry.getKey();
            List<Task> tasks = entry.getValue();

            double averageRating = tasks.stream()
                    .filter(task -> task.getRating() != null) 
                    .mapToInt(Task::getRating)
                    .average()
                    .orElse(0.0);

            long completedCount = tasks.size();

            volunteerStatsList.add(new VolunteerStatsDto(volunteer, averageRating, completedCount)); 
        }

        
        volunteerStatsList.sort((stats1, stats2) -> {
            int ratingCompare = Double.compare(stats2.getAverageRating(), stats1.getAverageRating());
            if (ratingCompare != 0) {
                return ratingCompare;
            }
            return Long.compare(stats2.getCompletedTasksCount(), stats1.getCompletedTasksCount()); 
        });


        return volunteerStatsList.stream()
                .map(VolunteerStatsDto::getVolunteer) 
                .collect(Collectors.toList());
    }
}