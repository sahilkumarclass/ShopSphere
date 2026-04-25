package com.shopsphere.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${shopsphere.notifications.from-address}")
    private String fromAddress;

    public void sendPlainEmail(String to, String subject, String body) {
        if (to == null || to.isBlank()) {
            log.warn("Skipping email send — no recipient");
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        log.info("Sent email to {} with subject \"{}\"", to, subject);
    }
}
