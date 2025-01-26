package com.blood.bank.Blood.bank.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private JavaMailSender eMailSender;

    public EmailService(JavaMailSender eMailSender) {
        this.eMailSender = eMailSender;
    }

    @Async
    public void sendVerificationEmail(String to, String subject, String text) throws MessagingException{
        MimeMessage eMessage = eMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(eMessage,true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text,true);

        eMailSender.send(eMessage);
    }
}
