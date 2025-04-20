package com.example.volunteer.service.auth.jwtToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

/**
 * Компонент для генерации, валидации и извлечения информации из JWT токенов
 * доступа.
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecretString;

    @Value("${jwt.access-token-expiration-ms:86400000}")
    private long jwtAccessExpirationInMs;

    private SecretKey jwtSecretKey;

    /**
     * Инициализирует секретный ключ JWT после создания бина.
     */
    @PostConstruct
    protected void init() {
        try {
            jwtSecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretString));
            logger.info("JWT Secret Key initialized successfully.");
        } catch (IllegalArgumentException e) {
            logger.error("Invalid Base64 encoded JWT secret key: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT secret key configuration", e);
        }
    }

    /**
     * Генерирует JWT токен доступа для указанного email пользователя (используется как subject).
     *
     * @param userEmail Email пользователя (будет записан в subject).
     * @return Сгенерированный JWT токен доступа.
     */
    public String generateAccessToken(String userEmail) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtAccessExpirationInMs);

        return Jwts.builder()
                .subject(userEmail) // Используем email как subject
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jwtSecretKey)
                .compact();
    }

    /**
     * Извлекает ID пользователя (который хранится как subject) из JWT токена.
     * ПРИМЕЧАНИЕ: В текущей реализации этот метод возвращает email, так как email хранится в subject.
     * Рекомендуется использовать getUserEmailFromToken для ясности или удалить этот метод.
     *
     * @param token JWT токен.
     * @return Email пользователя (извлеченный из subject).
     * @throws JwtException если токен невалиден или истек.
     */
    public String getUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException e) {
            logger.error("Error parsing JWT token to get user ID (subject): {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Извлекает email пользователя (который хранится как subject) из JWT токена.
     *
     * @param token JWT токен.
     * @return Email пользователя (извлеченный из subject).
     * @throws JwtException если токен невалиден или истек.
     */
    public String getUserEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (JwtException e) {
            logger.error("Error parsing JWT token to get user email (subject): {}", e.getMessage());
            throw e;
        }
    }


    /**
     * Валидирует JWT токен.
     * Проверяет подпись и срок действия токена.
     *
     * @param authToken JWT токен для валидации.
     * @return true, если токен валиден, иначе false.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(jwtSecretKey)
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            logger.warn("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }
}