package com.example.volunteer.service;

import com.example.volunteer.model.User;
import com.example.volunteer.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUser = new User("john@example.com", "John", "Doe", "Middle");
    }

    @Test
    void createUser_Success() {
        when(userRepository.existsById(sampleUser.getEmail())).thenReturn(false);
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);

        User created = userService.createUser(sampleUser);
        assertEquals("John", created.getFirstName());
    }

    @Test
    void createUser_AlreadyExists() {
        when(userRepository.existsById(sampleUser.getEmail())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(sampleUser));
    }

    @Test
    void updateUser_Success() {
        when(userRepository.findById(sampleUser.getEmail())).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User updated = userService.updateUser(sampleUser.getEmail(), new User("john@example.com", "Johnny", "Doe", "NewMiddle"));
        assertEquals("Johnny", updated.getFirstName());
        assertEquals("NewMiddle", updated.getMiddleName());
    }

    @Test
    void updateUser_NotFound() {
        when(userRepository.findById(sampleUser.getEmail())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userService.updateUser(sampleUser.getEmail(), sampleUser));
    }
}
