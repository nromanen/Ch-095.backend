package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.service.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@PropertySource("classpath:application.properties")
@Service
public class EmailServiceImpl implements EmailService {
    @Value("${app.frontend.url}")
    private String baseUrl;
    private static final String END_POINT = "test/";
    private final JavaMailSender javaMailSender;
    private final ContactService contactService;
    private final SurveyService surveyService;
    private final SurveyContactConnectorService surveyContactService;
    private final UserService userService;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, ContactService contactService, SurveyService surveyService, SurveyContactConnectorService surveyContactService, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.contactService = contactService;
        this.surveyService = surveyService;
        this.surveyContactService = surveyContactService;
        this.userService = userService;
    }

    private void sendMailWithLink(String userEmail, String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(userEmail);
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        javaMailSender.send(email);
    }

    public void sendEmailForUser(String idUser, String idSurvey, String anEmail) {
        Optional<Survey> survey = surveyService.findFirstById(Long.parseLong(idSurvey));
        Optional<User> user = userService.findFirstById(Long.valueOf(idUser));
        String userEmail = userService.getEmailByUserId(Long.valueOf(idUser));
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
        String message = "Message from " + userEmail + ": " + "<<Please, follow the link and take the survey" + " " + encodedString + " >>";
        sendMailWithLink(userEmail, anEmail, subject, message);
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
