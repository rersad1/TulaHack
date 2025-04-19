package com.example.volunteer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "volunteers")
public class Volunteer {

    @Id
    private String email;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private String middleName;

    @NonNull
    private Long organizationId;
}
