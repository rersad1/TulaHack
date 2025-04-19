package com.example.volunteer.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Сервис для отправки email сообщений, связанных с аутентификацией.
 */
@Service
public class AuthEmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Отправляет email с токеном для подтверждения входа.
     * 
     * @param to    Адрес получателя.
     * @param token Токен для входа.
     */
    public void sendLoginToken(String to, String token) {
        String subject = "Подтверждение входа";
        String body = "Для подтверждения входа используйте следующий код: " + token + "\n\n" +
                "Если вы не запрашивали вход, проигнорируйте это сообщение.";
        sendSimpleMessage(to, subject, body);
    }

    /**
     * Отправляет email со ссылкой для сброса пароля.
     * 
     * @param to        Адрес получателя.
     * @param resetLink Ссылка для сброса пароля.
     */
    public void sendPasswordResetToken(String to, String resetLink) {
        String subject = "Сброс пароля";
        String body = "Для сброса пароля перейдите по ссылке: " + resetLink + "\n\n" +
                "Если вы не запрашивали сброс пароля, проигнорируйте это сообщение.";
        sendSimpleMessage(to, subject, body);
    }

    /**
     * Отправляет email со ссылкой для завершения регистрации.
     * 
     * @param to               Адрес получателя.
     * @param registrationLink Ссылка для регистрации.
     */
    public void sendRegistrationLink(String to, String registrationLink) {
        String subject = "Регистрация";
        String body = "Для завершения регистрации перейдите по ссылке: " + registrationLink + "\n\n" +
                "Если вы не регистрировались, проигнорируйте это сообщение.";
        sendSimpleMessage(to, subject, body);
    }

    /**
     * Вспомогательный метод для отправки простого текстового email сообщения.
     * 
     * @param to      Адрес получателя.
     * @param subject Тема сообщения.
     * @param body    Тело сообщения.
     */
    private void sendSimpleMessage(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}