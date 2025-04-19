package com.example.volunteer.security;

import com.example.volunteer.service.auth.user.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List; // Импортируем List

/**
 * Конфигурация безопасности Spring Security.
 * Определяет правила доступа к эндпоинтам, настройки сессий,
 * провайдер аутентификации и фильтр JWT.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Включает аннотации безопасности на методах (например, @PreAuthorize)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Конфигурирует цепочку фильтров безопасности HTTP.
     * Отключает CSRF, настраивает CORS, правила авторизации запросов,
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
                // Отключаем CSRF, так как используем stateless аутентификацию (JWT)
                .csrf(AbstractHttpConfigurer::disable)
                // Применяем конфигурацию CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Настройка правил авторизации для HTTP запросов
                .authorizeHttpRequests(auth -> auth
                        // Разрешаем публичный доступ к эндпоинтам аутентификации и регистрации
                        .requestMatchers(HttpMethod.POST,
                                "/api/register",
                                "/api/login",
                                "/api/token-login",
                                "/api/refresh-token",
                                "/api/request-reset-password",
                                "/api/reset-password",
                                "/api/resend-verification")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/verify-email").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/volunteers/**").hasRole("VOLUNTEER") 
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "VOLUNTEER")
                        // Все остальные запросы требуют аутентификации
                        .anyRequest().authenticated())
                // Настраиваем управление сессиями: STATELESS, так как используем JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Устанавливаем кастомный провайдер аутентификации
                .authenticationProvider(authenticationProvider())
                // Добавляем JWT фильтр перед стандартным фильтром аутентификации по логину/паролю
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Конфигурация CORS для разрешения запросов с определенных источников (например, фронтенда).
     *
     * @return CorsConfigurationSource
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Укажите URL вашего фронтенда
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        // Разрешенные HTTP методы
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        // Разрешенные заголовки
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With"));
        // Разрешить отправку куки и заголовков авторизации
        configuration.setAllowCredentials(true);
        // Применить эту конфигурацию ко всем путям
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Создает и конфигурирует DaoAuthenticationProvider.
     * Устанавливает UserDetailsService (для загрузки данных пользователя)
     * и PasswordEncoder (для проверки паролей).
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
     * Предоставляет AuthenticationManager из конфигурации аутентификации Spring.
     * Необходим для некоторых процессов аутентификации.
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
     * Предоставляет бин PasswordEncoder (используем BCrypt).
     * Необходим для хеширования паролей при регистрации и проверки при входе.
     *
     * @return PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}