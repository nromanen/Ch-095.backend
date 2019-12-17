package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.service.db.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SendEmailController {

    @Autowired
    EmailService emailService;

    @PostMapping("/sendEmails")
    public String doSendEmails(@RequestBody List<String> emails) {
        String subject = "someSubject";
        String message = "someMessage";
        emails.forEach(email -> {
            emailService.sendMail(email, subject, message);
        });
        return "Result";
    }

    @PostMapping("/sendEmail")
    public String doSendEmail(@RequestBody EmailDTO emailDTO) {
        String to = emailDTO.getTo();
        String subject = emailDTO.getSubject();
        String message = emailDTO.getMessage();
        emailService.sendMail(to, subject, message);
        return "Result";
    }
}
