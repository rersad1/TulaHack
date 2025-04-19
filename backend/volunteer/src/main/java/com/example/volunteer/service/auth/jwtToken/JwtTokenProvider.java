package com.example.volunteer.service.auth.jwtToken;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
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
        // Преобразуем строку секрета в ключ
        jwtSecretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecretString));
    }

    /**
     * Генерирует JWT токен доступа для указанного ID пользователя.
     *
     * @param userId ID пользователя.
     * @return Сгенерированный JWT токен доступа.
     */
    public String generateAccessToken(String userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtAccessExpirationInMs);

        return Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(jwtSecretKey)
                .compact();
    }

    /**
     * Извлекает ID пользователя из JWT токена.
     *
     * @param token JWT токен.
     * @return ID пользователя.
     * @throws JwtException если токен невалиден или истек.
     */
    public String getUserIdFromToken(String token) {
        return Jwts.parser()
                .verifyWith(jwtSecretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
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
            System.err.println("Invalid JWT token: " + e.getMessage());
        }
        return false;
    }
}