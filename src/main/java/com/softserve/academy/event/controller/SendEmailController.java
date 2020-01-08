package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.exception.IncorrectEmailsException;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.util.EmailValidator;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PropertySource("classpath:application.properties")
public class SendEmailController {
    private final EmailService emailService;

    public SendEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/sendEmails")
    public String doSendEmails(@RequestBody EmailDTO emailDTO) {
        String[] emails = emailDTO.getEmailsArray();
        try {
            EmailValidator.validate(emails);
        } catch (IncorrectEmailsException e) {
            return e.getMessage();
        }
        String idUser = emailDTO.getUserId();
        String idSurvey = emailDTO.getSurveyId();
        for (String anEmail : emails) {
            emailService.sendEmailForUser(idUser, idSurvey, anEmail);
        }
        return null;
    }
}
