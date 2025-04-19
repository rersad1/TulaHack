package com.example.volunteer.controller;

import com.example.volunteer.model.Volunteer;
import com.example.volunteer.repository.VolunteerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VolunteerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final String URL = "/api/volunteers";

    @BeforeEach
    void setup() {
        volunteerRepository.deleteAll();
    }

    @Test
    void createVolunteer_thenSuccess() throws Exception {
        Volunteer volunteer = new Volunteer("test@vol.org", "Анна", "Смирнова", null, 100L);

        mockMvc.perform(post(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(volunteer)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("test@vol.org"))
            .andExpect(jsonPath("$.firstName").value("Анна"))
            .andExpect(jsonPath("$.organizationId").value(100));
    }

    @Test
    void updateVolunteer_thenSuccess() throws Exception {
        Volunteer volunteer = new Volunteer("edit@vol.org", "Алексей", "Попов", null, 101L);
        volunteerRepository.save(volunteer);

        volunteer.setFirstName("Александр");

        mockMvc.perform(put(URL + "/edit@vol.org")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(volunteer)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.firstName").value("Александр"));
    }

    @Test
    void updateVolunteer_notFound() throws Exception {
        Volunteer volunteer = new Volunteer("notfound@vol.org", "Елена", "Новикова", null, 102L);

        mockMvc.perform(put(URL + "/notfound@vol.org")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(volunteer)))
            .andExpect(status().isNotFound());
    }
}
