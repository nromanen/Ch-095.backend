package com.softserve.academy.event.controller;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import com.softserve.academy.event.service.db.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SendEmailController {
    private final ContactService contactService;
    private final SurveyService surveyService;
    private final SurveyContactConnectorService surveyContactService;

    @Autowired
    EmailService emailService;

    public SendEmailController(ContactService contactService, SurveyService surveyService, SurveyContactConnectorService surveyContactService) {
        this.contactService = contactService;
        this.surveyService = surveyService;
        this.surveyContactService = surveyContactService;
    }

//    @PostMapping("/sendEmails")
//    public String doSendEmails(@RequestBody List<String> emails) {
//        String subject = "someSubject";
//        String message = "someMessage";
//        emails.forEach(email -> {
//            emailService.sendMail(email, subject, message);
//        });
//        return "Result";
//    }
//
//    @PostMapping("/sendEmail")
//    public String doSendEmail(@RequestBody EmailDTO emailDTO) {
//        String to = emailDTO.getTo();
//        String subject = emailDTO.getSubject();
//        String message = emailDTO.getMessage();
//        emailService.sendMail(to, subject, message);
//        return "Result";
//    }

//    @GetMapping(value = "/getTitle")
//    public String getTestsBySurvey(@RequestParam Long surveyId){
//        String title = null;
//        return  title;
//    }

//    @PostMapping("/sendEmails")
//    public String doSendEmails(@RequestBody List<String> emails) {
//        String subject = "someSubject";
//        String message = "someMessage";
//        emails.forEach(email -> {
//            emailService.sendMail(email, subject, message);
//        });
//        return "Result";
//    }


//    @PostMapping("/sendEmail")
//    public String doSendEmail(@RequestBody EmailDTO emailDTO) {
//        String to = emailDTO.getTo();
//        String subject = emailDTO.getSubject();
//        String message = emailDTO.getMessage();
//        emailService.sendMail(to, subject, message);
//        return "Result";
//    }
//


    @PostMapping("/sendEmails")
    public String doSendEmails(@RequestBody String emails) {
        String email[] = emails.split(",");
        for (String anEmail : email) {

            contactService.saveEmail(anEmail);

//          Optional<Long> idByEmail = contactService.getIdByEmail(anEmail);
            String idSurvey = "1";

            Survey survey = new Survey();
            survey.setId(Long.valueOf(idSurvey));

            Contact contact = new Contact();
            contact.setEmail(anEmail);
            Set<Contact> contactSet = new HashSet<>();
            contactSet.add(contact);
            surveyService.save(contactSet);

            surveyContactService.addRow(survey, contact);
            String codEmail = anEmail + ";" + idSurvey;
            String encodedString = "http://localhost:8081/testAccess/" + Base64.getEncoder().withoutPadding().encodeToString(codEmail.getBytes());
            String subject = "nameOfSurvey";
            String message = "Please, follow the link and take the survey" + " " + encodedString;
            emailService.sendMail(anEmail, subject, message);
        }
        return "Works, don't delete!";
    }

}