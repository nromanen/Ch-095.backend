package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.exception.IncorrectEmailsException;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SendEmailController {
    private final EmailService emailService;
    private final UserService service;

    @Autowired
    public SendEmailController(EmailService emailService, UserService service) {
        this.service = service;
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
        String idUser = service.getAuthenticationId().orElseThrow(UserNotFound::new).toString();
        String idSurvey = emailDTO.getSurveyId();
        for (String anEmail : emails) {
            emailService.sendEmailForUser(idUser, idSurvey, anEmail);
        }
        return null;
    }
}