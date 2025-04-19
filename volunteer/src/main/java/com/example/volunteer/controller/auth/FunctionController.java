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

/**
 * Контроллер для обработки запросов аутентификации и управления пользователями.
 * Предоставляет эндпоинты для регистрации, верификации email, смены и сброса
 * пароля,
 * входа в систему и обновления JWT токенов.
 */
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

    /**
     * Эндпоинт для регистрации нового пользователя.
     * Отправляет ссылку для подтверждения аккаунта на указанный email.
     *
     * @param registrationDTO DTO с данными для регистрации пользователя.
     * @return ResponseEntity со статусом CREATED и сообщением об отправке ссылки.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        userRegistrationService.registerUser(registrationDTO);
        return new ResponseEntity<>("Ссылка для подтверждения аккаунта отправлена на почту", HttpStatus.CREATED);
    }

    /**
     * Эндпоинт для подтверждения email пользователя по токену.
     *
     * @param token Токен верификации, полученный из письма.
     * @return ResponseEntity со статусом OK и сообщением об успешном подтверждении.
     */
    // TODO: логинить акк после подтверждения
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        emailVerificationService.verifyEmail(token);
        return new ResponseEntity<>("Email успешно подтвержден", HttpStatus.OK);
    }

    /**
     * Эндпоинт для повторной отправки письма с подтверждением email.
     *
     * @param request Map содержащий email пользователя.
     * @return ResponseEntity со статусом OK и сообщением о повторной отправке.
     */
    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        emailVerificationService.resendVerificationEmail(email);
        return new ResponseEntity<>("Письмо с подтверждением отправлено повторно", HttpStatus.OK);
    }

    /**
     * Эндпоинт для смены пароля аутентифицированного пользователя.
     *
     * @param request DTO с email, старым и новым паролями.
     * @return ResponseEntity со статусом OK.
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDTO request) {
        userChangePasswordService.changePassword(request.getEmail(), request.getOldPassword(),
                request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Эндпоинт для запроса сброса пароля.
     * Отправляет ссылку для сброса пароля на указанный email.
     *
     * @param request DTO с email пользователя.
     * @return ResponseEntity со статусом OK и сообщением об отправке ссылки.
     */
    @PostMapping("/request-reset-password")
    public ResponseEntity<?> requestResetPassword(@RequestBody ResetPasswordRequestDTO request) {
        passwordResetService.requestPasswordReset(request.getEmail());
        return new ResponseEntity<>("Ссылка для сброса пароля отправлена на вашу почту", HttpStatus.OK);
    }

    /**
     * Эндпоинт для установки нового пароля после запроса на сброс.
     *
     * @param request DTO с токеном сброса и новым паролем.
     * @return ResponseEntity со статусом OK.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Эндпоинт для входа пользователя в систему по email и паролю.
     * Отправляет одноразовый код для входа на email пользователя.
     *
     * @param loginRequest DTO с email и паролем пользователя.
     * @return ResponseEntity со статусом OK и сообщением об отправке кода.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        String token = userLoginService.login(loginRequest.getEmail(), loginRequest.getPassword());
        authEmailService.sendLoginToken(loginRequest.getEmail(), token);
        return new ResponseEntity<>("Единоразовый код для входа отправлен вам на почту", HttpStatus.OK);
    }

    /**
     * Эндпоинт для входа пользователя с использованием одноразового токена.
     * Валидирует токен и выдает JWT access и refresh токены.
     *
     * @param request DTO с одноразовым токеном.
     * @return ResponseEntity со статусом OK и DTO с JWT токенами.
     */
    @PostMapping("/token-login")
    public ResponseEntity<LoginResponseDTO> tokenLogin(@RequestBody LoginTokenRequestDTO request) {
        User user = tokenService.validateToken(request.getToken());
        LoginResponseDTO tokens = jwtAuthService.issueTokens(user);
        return ResponseEntity.ok(tokens);
    }

    /**
     * Эндпоинт для обновления JWT токенов с использованием refresh токена.
     *
     * @param request DTO с refresh токеном.
     * @return ResponseEntity со статусом OK и DTO с новыми JWT токенами.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO request) {
        String requestRefreshToken = request.getRefreshToken();
        LoginResponseDTO newTokens = jwtAuthService.refreshToken(requestRefreshToken);
        return ResponseEntity.ok(newTokens);
    }

}