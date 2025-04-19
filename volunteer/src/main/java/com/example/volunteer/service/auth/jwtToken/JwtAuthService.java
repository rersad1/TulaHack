package com.example.volunteer.service.auth.jwtToken;

import com.example.volunteer.DTO.auth.LoginResponseDTO;
import com.example.volunteer.exceptions.auth.RefreshTokenExpiredException;
import com.example.volunteer.exceptions.auth.RefreshTokenNotFoundException;
import com.example.volunteer.model.auth.JwtRefreshToken;
import com.example.volunteer.model.auth.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: поправить обработку ошибок

@Service
public class JwtAuthService {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private JwtRefreshTokenService jwtRefreshTokenService;

    public LoginResponseDTO issueTokens(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
        String refreshToken = jwtRefreshTokenService.createRefreshToken(user).getToken();
        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
    JwtRefreshToken foundToken = jwtRefreshTokenService.findByToken(refreshToken).orElseThrow(() -> new RefreshTokenNotFoundException());

    if (jwtRefreshTokenService.isExpired(foundToken)) {
        jwtRefreshTokenService.deleteById(foundToken.getId()); // Удаляем истекший токен
        throw new RefreshTokenExpiredException();
    }

    User user = foundToken.getUser();
    return issueTokens(user);
}
}