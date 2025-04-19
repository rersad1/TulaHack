package com.example.volunteer.security;

import com.example.volunteer.service.auth.jwtToken.JwtTokenProvider;
import com.example.volunteer.service.auth.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Регистрируем фильтр как компонент Spring
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Добавляем логгер
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // Сервис для загрузки данных пользователя

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            // Проверяем, есть ли токен и валиден ли он
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Получаем email пользователя (username) из токена
                String userEmail = tokenProvider.getUserIdFromToken(jwt); // Метод возвращает email

                // Загружаем детали пользователя по email (username)
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail); // Используем стандартный метод

                // Создаем объект аутентификации
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Устанавливаем аутентификацию в SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        catch (Exception ex) {
            // Логирование ошибки, если необходимо
            // Убедитесь, что UsernameNotFoundException из loadUserByUsername здесь корректно обрабатывается
            // или пробрасывается дальше для обработки стандартными механизмами Spring Security
            logger.error("Could not set user authentication in security context", ex);
        }

        // Продолжаем цепочку фильтров
        filterChain.doFilter(request, response);
    }

    // Вспомогательный метод для извлечения JWT из заголовка Authorization
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Убираем "Bearer "
        }
        return null;
    }
}