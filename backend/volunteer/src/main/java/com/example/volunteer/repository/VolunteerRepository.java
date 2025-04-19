package com.example.volunteer.repository;

import com.example.volunteer.model.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerRepository extends JpaRepository<Volunteer, String> {
    // email используется как идентификатор
}
