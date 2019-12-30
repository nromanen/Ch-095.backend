package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.service.db.*;
import com.softserve.academy.event.util.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

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

//    @CrossOrigin(origins = "http://localhost:4200/sendForm/surveyTitle")
//    @GetMapping("/surveyTitle")
//    @ResponseBody
//    public ResponseEntity<AnswerSurveyTitle> getSurveyTitle(
//            @RequestParam(name = "surveyId") String surveyId) {
//        Optional<Survey> surveyOptional = surveyService.findFirstById(Long.parseLong(surveyId));
//        return surveyOptional.map(survey -> new ResponseEntity<>(new AnswerSurveyTitle(
//                survey.getTitle()), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
//    }

    @PostMapping("/sendEmails")
    public String doSendEmails(@RequestBody EmailDTO emailDTO) {
        String[] emails = emailDTO.getEmailsArray();
        EmailValidator.validate(emails);
        String idUser = emailDTO.getUserId();
        String idSurvey = emailDTO.getSurveyId();
        for (String anEmail : emails) {
            Optional<Survey> survey = surveyService.findFirstById(Long.parseLong(idSurvey));
            Optional<User> user = userService.findFirstById(Long.valueOf(idUser));
            String userEmail = userService.getEmailByUserId(Long.valueOf(idUser));
            Contact contact = new Contact();
            if (user.isPresent()) {
                contact.setUser(user.get());
                contact.setEmail(anEmail);
                contactService.save(contact);
            }
            SurveyContact surveyContact = new SurveyContact();
            if (survey.isPresent()) {
                surveyContact.setContact(contact);
                surveyContact.setSurvey(survey.get());
                surveyContact.setCanPass(true);
                surveyContactService.save(surveyContact);
            }
            String codEmail = anEmail + ";" + idSurvey;
            String encodedString = baseUrl + END_POINT + Base64.getEncoder().withoutPadding().encodeToString(codEmail.getBytes());
            String subject = "Survey";
            String message = "Message from " + userEmail + ": " + "<<Please, follow the link and take the survey" + " " + encodedString + " >>";
            emailService.sendMailWithLink(userEmail, anEmail, subject, message);
        }
        return "Works, don't delete!";
    }

}
