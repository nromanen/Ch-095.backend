package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.service.db.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMailWithLink(String userEmail, String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(userEmail);
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }

    @Override
    public boolean isValid(String email) {

            return true;

    }

    @Override
    public void sendMail(String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }
}
