package com.example.volunteer.service;

import com.example.volunteer.model.Volunteer;
import com.example.volunteer.repository.VolunteerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;

    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    public Volunteer createVolunteer(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    public Optional<Volunteer> updateVolunteer(String email, Volunteer updatedVolunteer) {
        return volunteerRepository.findById(email).map(existing -> {
            existing.setFirstName(updatedVolunteer.getFirstName());
            existing.setLastName(updatedVolunteer.getLastName());
            existing.setMiddleName(updatedVolunteer.getMiddleName());
            existing.setOrganizationId(updatedVolunteer.getOrganizationId());
            return volunteerRepository.save(existing);
        });
    }
}
