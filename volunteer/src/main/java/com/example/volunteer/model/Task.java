package com.example.volunteer.model;

import com.example.volunteer.model.auth.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "task_responses",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> responders = new HashSet<>();
}