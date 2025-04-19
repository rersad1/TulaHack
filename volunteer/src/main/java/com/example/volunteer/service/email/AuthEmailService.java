package com.example.volunteer.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class AuthEmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    public void sendLoginToken(String to, String token) {
        String subject = "Подтверждение входа";
        String body = "Для подтверждения входа используйте следующий код: " + token + "\n\n" +
                    "Если вы не запрашивали вход, проигнорируйте это сообщение.";
        sendSimpleMessage(to, subject, body);
    }
    
    public void sendPasswordResetToken(String to, String resetLink) {
        String subject = "Сброс пароля";
        String body = "Для сброса пароля перейдите по ссылке: " + resetLink + "\n\n" +
                    "Если вы не запрашивали сброс пароля, проигнорируйте это сообщение.";
        sendSimpleMessage(to, subject, body);
    }

    public void sendRegistrationLink(String to, String registrationLink) {
        String subject = "Регистрация";
        String body = "Для завершения регистрации перейдите по ссылке: " + registrationLink + "\n\n" +
                    "Если вы не регистрировались, проигнорируйте это сообщение.";
        sendSimpleMessage(to, subject, body);
    }
    
    private void sendSimpleMessage(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}