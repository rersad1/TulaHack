package com.example.volunteer.controller;

import com.example.volunteer.model.Task;
import com.example.volunteer.model.TaskStatus;
import com.example.volunteer.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.volunteer.model.TaskCategory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String URL = "/api/tasks";

    @BeforeEach
    void setup() {
        taskRepository.deleteAll();
    }

    @Test
void createTask_thenSuccess() throws Exception {
    Task task = new Task(null, "Помощь", "Помочь бабушке", TaskStatus.OPEN,
            TaskCategory.SOCIAL, "user@example.com", null, null);

    mockMvc.perform(post(URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("OPEN"))
        .andExpect(jsonPath("$.category").value("SOCIAL"));
}

    @Test
void updateTask_thenSuccess() throws Exception {
    Task task = new Task(null, "Уборка", "Субботник в парке", TaskStatus.OPEN,
            TaskCategory.ECOLOGY, "eco@example.com", null, null);
    task = taskRepository.save(task);

    task.setStatus(TaskStatus.COMPLETED);
    task.setRating(5);
    task.setUserComment("Спасибо!");

    mockMvc.perform(put(URL + "/" + task.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(task)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("COMPLETED"))
        .andExpect(jsonPath("$.category").value("ECOLOGY"))
        .andExpect(jsonPath("$.rating").value(5));
}
    @Test
    void updateTask_notFound() throws Exception {
        Task task = new Task(null, "123", "Описание", TaskStatus.OPEN,
                TaskCategory.OTHER, "user@mail.com", null, null);

        mockMvc.perform(put(URL + "/9999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
            .andExpect(status().isNotFound());
    }
}
