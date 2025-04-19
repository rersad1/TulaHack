// filepath: /home/rersad/projects/TulaHack/volunteer/src/main/java/com/example/volunteer/security/SecurityConfig.java
package com.example.volunteer.security;

import com.example.volunteer.service.auth.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности Spring Security.
 * Определяет правила доступа к эндпоинтам, настройки сессий,
 * провайдер аутентификации и фильтр JWT.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Конфигурирует цепочку фильтров безопасности HTTP.
     * Отключает CSRF, настраивает правила авторизации запросов,
     * устанавливает политику управления сессиями (stateless),
     * добавляет провайдер аутентификации и JWT фильтр.
     *
     * @param http HttpSecurity объект для конфигурации.
     * @return Сконфигурированный SecurityFilterChain.
     * @throws Exception если возникает ошибка при конфигурации.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF отключен
                .authorizeHttpRequests(auth -> auth
                        // Эндпоинты, доступные всем
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/register",
                                "/api/verify-email", // GET-запрос для верификации
                                "/api/forgot-password", // Вероятно, /api/request-reset-password
                                "/api/reset-password",
                                "/api/login",
                                "/api/token-login", // POST-запрос для входа по токену
                                "/api/refresh-token")
                        .permitAll()
                        // Правила для других эндпоинтов
                        .requestMatchers("/api/volunteers/**").hasRole("VOLUNTEER")
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "VOLUNTEER")
                        // Все остальные требуют аутентификации
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Создает и конфигурирует DaoAuthenticationProvider.
     * Устанавливает UserDetailsService и PasswordEncoder.
     *
     * @return Сконфигурированный AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Предоставляет AuthenticationManager из конфигурации аутентификации.
     *
     * @param config AuthenticationConfiguration.
     * @return AuthenticationManager.
     * @throws Exception если возникает ошибка при получении AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Предоставляет бин PasswordEncoder (BCryptPasswordEncoder).
     *
     * @return PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}