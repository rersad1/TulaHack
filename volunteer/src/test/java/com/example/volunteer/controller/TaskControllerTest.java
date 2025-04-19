import com.example.volunteer.model.TaskCategory;
// ...

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
