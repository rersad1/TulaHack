package com.example.volunteer.model.auth;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    // Определение колонки остается прежним, если оно вам нужно
    @Column(name = "id", columnDefinition = "VARCHAR(36)", updatable = false, nullable = false, unique = true)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = true)
    private String authToken;

    @Column(nullable = true)
    private LocalDateTime tokenCreatedAt;

    @Column(nullable = false)
    private Boolean enabled = false;

    public boolean isEnabled() {
        return Boolean.TRUE.equals(this.enabled);
    }

}