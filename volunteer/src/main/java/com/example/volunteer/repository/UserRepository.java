package com.example.volunteer.repository;

import com.example.volunteer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // email используется как идентификатор
}
