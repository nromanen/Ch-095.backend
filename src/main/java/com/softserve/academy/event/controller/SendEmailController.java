package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.exception.IncorrectEmailsException;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.EmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
public class SendEmailController {
    private final EmailService emailService;
    private final UserService service;
    private final ContactService contactService;

    @Autowired
    public SendEmailController(EmailService emailService, UserService service, ContactService contactService) {
        this.service = service;
        this.emailService = emailService;
        this.contactService = contactService;
    }

    @PostMapping("/sendEmails")
    public void doSendEmails(@RequestBody EmailDTO emailDTO) {
        String[] emails = emailDTO.getEmailsArray();
        EmailValidator.validate(emails);
        String idUser = service.getAuthenticationId().orElseThrow(UserNotFound::new).toString();
        String idSurvey = emailDTO.getSurveyId();
        emailService.sendEmailForUser(idUser, idSurvey, emails);
    }

    @ExceptionHandler(IncorrectEmailsException.class)
    public ResponseEntity<String> incorrectEmailsHandler(Exception e, WebRequest request) {
        log.error("Incorrect emails : ", e);
        log.error(request.getDescription(false));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/contacts")
    public List<String> listOfContacts(){
        String userId = service.getAuthenticationId().orElseThrow(UserNotFound::new).toString();
        List<String> contacts = contactService.listContactsByUserId(Long.valueOf(userId));
        return contacts;
    }
}
