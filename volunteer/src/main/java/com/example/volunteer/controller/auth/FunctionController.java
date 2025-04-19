package com.example.volunteer.controller.auth;

import com.example.volunteer.DTO.auth.*;
import com.example.volunteer.service.auth.jwtToken.JwtAuthService;
import com.example.volunteer.service.auth.password.PasswordResetService;
import com.example.volunteer.service.auth.token.TokenService;
import com.example.volunteer.service.auth.user.EmailVerificationService;
import com.example.volunteer.service.auth.user.UserChangePasswordService;
import com.example.volunteer.service.auth.user.UserLoginService;
import com.example.volunteer.service.auth.user.UserRegistrationService;
import com.example.volunteer.service.email.AuthEmailService;

import jakarta.validation.Valid;

import com.example.volunteer.model.auth.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class FunctionController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserChangePasswordService userChangePasswordService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private AuthEmailService authEmailService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailVerificationService emailVerificationService;

    @Autowired
    private JwtAuthService jwtAuthService;

    // Эндпоинт для регистрации пользователя
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        userRegistrationService.registerUser(registrationDTO);
        return new ResponseEntity<>("Ссылка для подтверждения аккаунта отправлена на почту", HttpStatus.CREATED);
    }

    // Эндпоинт для подтверждения email
    // TODO: логинить акк после подтверждения
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        emailVerificationService.verifyEmail(token);
        return new ResponseEntity<>("Email успешно подтвержден", HttpStatus.OK);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        emailVerificationService.resendVerificationEmail(email);
        return new ResponseEntity<>("Письмо с подтверждением отправлено повторно", HttpStatus.OK);
    }

    // Эндпоинт для смены пароля
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
        userChangePasswordService.changePassword(request.getEmail(), request.getOldPassword(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Эндпоинт для запроса сброса пароля
    @PostMapping("/request-reset-password")
    public ResponseEntity<?> requestResetPassword(@RequestBody ResetPasswordRequestDTO request) {
        passwordResetService.requestPasswordReset(request.getEmail());
        return new ResponseEntity<>("Ссылка для сброса пароля отправлена на вашу почту", HttpStatus.OK);
    }

    // Эндпоинт для сброса пароля
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Эндпоинт для логина             
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        String token = userLoginService.login(loginRequest.getEmail(), loginRequest.getPassword());
        authEmailService.sendLoginToken(loginRequest.getEmail(), token);
        return new ResponseEntity<>("Единоразовый код для входа отправлен вам на почту", HttpStatus.OK);
    }
    
    @PostMapping("/token-login")
    public ResponseEntity<LoginResponseDTO> tokenLogin(@RequestBody LoginTokenRequestDTO request) {
        User user = tokenService.validateToken(request.getToken());
        LoginResponseDTO tokens = jwtAuthService.issueTokens(user);
        return ResponseEntity.ok(tokens);
    }

    // Эндпоинт для обновления JWT токенов с использованием refresh токена
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();
        LoginResponseDTO newTokens = jwtAuthService.refreshToken(requestRefreshToken);
        return ResponseEntity.ok(newTokens);
    }

}