// filepath: /home/rersad/projects/TulaHack/volunteer/src/main/java/com/example/volunteer/service/auth/user/CustomUserDetailsService.java
package com.example.volunteer.service.auth.user;

import com.example.volunteer.model.auth.User;
import com.example.volunteer.repository.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Реализация UserDetailsService для загрузки пользовательских данных для Spring
 * Security.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Загружает данные пользователя по его email (используется как username).
     *
     * @param email Email пользователя.
     * @return UserDetails объект с данными пользователя.
     * @throws UsernameNotFoundException если пользователь с таким email не найден.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)); // TODO:
                                                                                                          // исправить
                                                                                                          // обработку
                                                                                                          // ошибок

        List<GrantedAuthority> authorities = Collections
                .singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true,
                true,
                true,
                authorities);
    }
}