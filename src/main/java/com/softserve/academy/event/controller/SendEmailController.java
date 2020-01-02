package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.service.db.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Optional;

@RestController
@PropertySource("classpath:application.properties")
@CrossOrigin(origins = "http://localhost:4200")
public class SendEmailController {

    @Value("${app.frontend.url}")
    private String baseUrl;
    private static final String END_POINT = "test/";

    private final ContactService contactService;
    private final SurveyService surveyService;
    private final SurveyContactConnectorService surveyContactService;
    private final UserService userService;
    private final EmailService emailService;

    public SendEmailController(ContactService contactService, SurveyService surveyService, SurveyContactConnectorService surveyContactService, UserService userService, EmailService emailService) {
        this.contactService = contactService;
        this.surveyService = surveyService;
        this.surveyContactService = surveyContactService;
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/sendEmails")
    public String doSendEmails(@RequestBody EmailDTO emailDTO) {
        String[] email = emailDTO.getEmails().split(",");
        String idUser = emailDTO.getUserId();
        String idSurvey = emailDTO.getSurveyId();
        for (String anEmail : email) {
            Optional<Survey> survey = surveyService.findFirstById(Long.parseLong(idSurvey));
            Optional<User> user = userService.findFirstById(Long.valueOf(idUser));
            Contact contact = new Contact();
            contact.setUser(user.orElseThrow(UserNotFound::new));
            contact.setEmail(anEmail);
            contactService.save(contact);
            SurveyContact surveyContact = new SurveyContact();
            surveyContact.setContact(contact);
            surveyContact.setSurvey(survey.orElseThrow(SurveyNotFound::new));
            surveyContact.setCanPass(true);
            surveyContactService.save(surveyContact);
            String codEmail = anEmail + ";" + idSurvey;
            String encodedString = baseUrl + END_POINT + Base64.getEncoder().withoutPadding().encodeToString(codEmail.getBytes());
            String subject = "Survey";
            String message = "Please, follow the link and take the survey" + " " + encodedString;
            emailService.sendMail(anEmail, subject, message);
        }
        return "Works, don't delete!";
    }

}
