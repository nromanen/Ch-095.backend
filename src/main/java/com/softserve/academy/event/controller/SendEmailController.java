package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.service.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SendEmailController {
    private final ContactService contactService;
    private final SurveyService surveyService;
    private final SurveyContactConnectorService surveyContactService;
    private final UserService userService;

    @Autowired
    EmailService emailService;

    public SendEmailController(ContactService contactService, SurveyService surveyService, SurveyContactConnectorService surveyContactService, UserService userService) {
        this.contactService = contactService;
        this.surveyService = surveyService;
        this.surveyContactService = surveyContactService;
        this.userService = userService;
    }

    @PostMapping("/sendEmails")
    public String doSendEmails(@RequestBody EmailDTO emailDTO) {
        String email[] = emailDTO.getEmails().split(",");
        String idUser = emailDTO.getUserId();
        String idSurvey = emailDTO.getSurveyId();
        for (String anEmail : email) {
            Optional<Survey> survey = surveyService.findFirstById(Long.valueOf(idSurvey));
            Optional<User> user = userService.findFirstById(Long.valueOf(idUser));
            Contact contact = new Contact();
            contact.setUser(user.get());
            contact.setEmail(anEmail);
            contactService.saveEmail(anEmail, user.get());
            surveyContactService.addRow(contact, survey.get());
            String codEmail = anEmail + ";" + idSurvey;
            String encodedString = "http://localhost:8081/testAccess/" + Base64.getEncoder().withoutPadding().encodeToString(codEmail.getBytes());
            String subject = "Survey";
            String message = "Please, follow the link and take the survey" + " " + encodedString;
            emailService.sendMail(anEmail, subject, message);
        }
        return "Works, don't delete!";
    }

}