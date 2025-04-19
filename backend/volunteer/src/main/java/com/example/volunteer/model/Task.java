package com.example.volunteer.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String title;

    @NonNull
    @Column(length = 2000)
    private String description;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TaskCategory category;

    @NonNull
    private String userEmail;

    private Integer rating;        
    private String userComment;
}
