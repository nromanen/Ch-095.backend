package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.exception.ContactNotFound;
import com.softserve.academy.event.exception.IncorrectEmailsException;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.EmailValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class SendEmailController {
    private final EmailService emailService;
    private final UserService service;
    private final ContactService contactService;
    private final SurveyContactConnectorService surveyContactService;

    @Autowired
    public SendEmailController(EmailService emailService, UserService service, ContactService contactService, SurveyContactConnectorService surveyContactService) {
        this.service = service;
        this.emailService = emailService;
        this.contactService = contactService;
        this.surveyContactService = surveyContactService;
    }

    @PostMapping("/sendEmails")
    public void doSendEmails(@RequestBody EmailDTO emailDTO) {
        String[] emails = emailDTO.getEmailsArray();
        EmailValidator.validate(emails);
        String idUser = service.getAuthenticationId().orElseThrow(UserNotFound::new).toString();
        String idSurvey = emailDTO.getSurveyId();
        emailService.sendEmailForUser(idUser, idSurvey, emails);
    }

    @PostMapping("/sendSelectedEmails")
    public void doSendSelectedEmails(@RequestBody EmailDTO emailDTO) {
        String[] emails = emailDTO.getEmailsArray();
        String idUser = service.getAuthenticationId().orElseThrow(UserNotFound::new).toString();
        String idSurvey = emailDTO.getSurveyId();
        emailService.sendSelectedEmailForUser(idUser, idSurvey, emails);
    }

    @ExceptionHandler(IncorrectEmailsException.class)
    public ResponseEntity<String> incorrectEmailsHandler(Exception e, WebRequest request) {
        log.error("Incorrect emails : ", e);
        log.error(request.getDescription(false));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/availableContacts")
    public List<String> listOfAvailableContacts(Long surveyId) {
        List<SurveyContact> listOfNotAvailableSurveyContacts = new ArrayList<>();
        List<Long> idListOfNotAvailableContacts = new ArrayList<>();
        String userId = service.getAuthenticationId().orElseThrow(UserNotFound::new).toString();
        List<Contact> allContactsOfUser = contactService.listContactsByUserId(Long.valueOf(userId));  //список всіх контактів по юзеру
        List<Long> idListOfAllContacts = allContactsOfUser.stream().map(Contact::getId).collect(Collectors.toList()); //список всіх id цих контактів
        for (Long contactId : idListOfAllContacts) {
            SurveyContact surveyContact = surveyContactService.surveyContactsByContactId(contactId, surveyId); //пара по (contactId, surveyId), що є у surveyContact
            if (surveyContact != null) {
                listOfNotAvailableSurveyContacts.add(surveyContact);
            }
        }
        for (SurveyContact surveyContact : listOfNotAvailableSurveyContacts) {
            Long idContact = surveyContact.getContact().getId();
            idListOfNotAvailableContacts.add(idContact);  // список id контактів, які треба виключити
        }
        //select * from contacts where contacts.id not in(select contact_id from survey_contacts where survey_id=2) and contacts.user_id=1;
        List<Contact> listOfContact = idListOfNotAvailableContacts.stream().map(e -> contactService.findFirstById(e).orElseThrow(ContactNotFound::new)).collect(Collectors.toList()); // список контактів, які треба виключити
        return allContactsOfUser.stream().filter(contact -> !listOfContact.contains(contact)).map(Contact::getEmail).collect(Collectors.toList());
    }
}
