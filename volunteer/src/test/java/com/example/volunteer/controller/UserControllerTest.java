package com.example.volunteer.controller;

import com.example.volunteer.model.User;
import com.example.volunteer.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String URL = "/api/users";

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_thenSuccess() throws Exception {
        User user = new User("test@example.com", "Test", "User", null);

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void updateUser_thenSuccess() throws Exception {
        User user = new User("edit@example.com", "Edit", "User", null);
        userRepository.save(user);

        user.setFirstName("Edited");

        mockMvc.perform(put(URL + "/edit@example.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Edited"));
    }
}
