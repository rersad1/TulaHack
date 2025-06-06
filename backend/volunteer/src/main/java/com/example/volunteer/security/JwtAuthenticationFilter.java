package com.example.volunteer.security;

import com.example.volunteer.service.auth.jwtToken.JwtTokenProvider;
import com.example.volunteer.service.auth.user.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Фильтр Spring Security для обработки JWT аутентификации.
 * Извлекает JWT токен из заголовка Authorization, валидирует его
 * и устанавливает аутентификацию пользователя в SecurityContextHolder,
 * пропуская публичные эндпоинты.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    // Список публичных путей, которые не требуют проверки JWT
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/register",
            "/api/login",
            "/api/token-login",
            "/api/refresh-token",
            "/api/request-reset-password",
            "/api/reset-password",
            "/api/resend-verification",
            "/api/verify-email",
            "/swagger-ui",
            "/v3/api-docs"
            
    );

    /**
     * Основной метод фильтра, выполняющийся для каждого запроса.
     * Пропускает публичные эндпоинты. Для остальных извлекает JWT токен,
     * валидирует его, загружает данные пользователя и устанавливает
     * аутентификацию в контексте безопасности.
     *
     * @param request     HttpServletRequest.
     * @param response    HttpServletResponse.
     * @param filterChain FilterChain для передачи запроса дальше по цепочке.
     * @throws ServletException если возникает ошибка сервлета.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Пропускаем preflight OPTIONS запросы
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();

        // Проверяем, является ли путь публичным
        boolean isPublic = PUBLIC_PATHS.stream().anyMatch(publicPath -> requestURI.startsWith(publicPath) || requestURI.matches(publicPath.replace("**", ".*"))); // Улучшенная проверка для Swagger
        if (isPublic) {
            logger.trace("Request URI {} is public, skipping JWT filter.", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        logger.debug("Request URI {} is not public, processing JWT filter.", requestURI);

        try {
            String jwt = getJwtFromRequest(request);

            // Проверяем, есть ли токен и валиден ли он
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Получаем email пользователя (username) из токена
                String userEmail = tokenProvider.getUserEmailFromToken(jwt);

                // Проверяем, что email получен и пользователь еще не аутентифицирован
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Загружаем детали пользователя по email (username)
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);

                    // Создаем объект аутентификации
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Устанавливаем аутентификацию в SecurityContext
                    logger.debug("Successfully authenticated user '{}' with roles {}", userEmail, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else if (userEmail == null) {
                    logger.warn("Could not get user email from JWT token.");
                }
                // Если пользователь уже аутентифицирован, ничего не делаем
                else if (SecurityContextHolder.getContext().getAuthentication() != null) {
                     logger.trace("User already authenticated, skipping JWT authentication setting.");
                }
            }
            else {
                if (!StringUtils.hasText(jwt)) {
                    logger.debug("JWT token is missing from the request.");
                } else {
                    // validateToken уже залогировал причину невалидности
                    logger.debug("JWT token is invalid or expired.");
                }
            }
        }
        catch (Exception ex) {
            // Ловим все исключения, чтобы гарантировать вызов filterChain.doFilter
            // и не прерывать обработку запроса из-за ошибки аутентификации
            logger.error("Could not set user authentication in security context", ex);
            // Очищаем контекст на случай частичной установки аутентификации
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Вспомогательный метод для извлечения JWT из заголовка Authorization.
     * Ожидает формат "Bearer {token}".
     *
     * @param request HttpServletRequest.
     * @return Строка с JWT токеном или null, если токен отсутствует или имеет
     *         неверный формат.
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Убираем "Bearer "
        }
        logger.trace("No JWT token found in Authorization header for request URI: {}", request.getRequestURI());
        return null;
    }
}