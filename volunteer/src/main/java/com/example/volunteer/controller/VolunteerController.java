package com.example.volunteer.controller;

import com.example.volunteer.model.Volunteer;
import com.example.volunteer.service.VolunteerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/volunteers")
public class VolunteerController {

    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping
    public ResponseEntity<Volunteer> createVolunteer(@RequestBody Volunteer volunteer) {
        Volunteer created = volunteerService.createVolunteer(volunteer);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{email}")
    public ResponseEntity<Volunteer> updateVolunteer(
            @PathVariable String email,
            @RequestBody Volunteer updatedVolunteer) {
        return volunteerService.updateVolunteer(email, updatedVolunteer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
