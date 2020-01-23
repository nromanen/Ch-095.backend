package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.exception.*;
import com.softserve.academy.event.repository.ContactRepository;
import com.softserve.academy.event.repository.SurveyContactConnectorRepository;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.db.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Optional;

@PropertySource("classpath:application.properties")
@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    String subject = "Survey";

    @Value("${app.frontend.url}")
    private String BASE_URL;
    private static final String END_POINT = "/test/";
    private final JavaMailSender javaMailSender;
    private final SurveyRepository surveyRepository;
    private final ContactRepository contactRepository;
    private final SurveyContactConnectorRepository surveyContactRepository;
    private final UserRepository userRepository;

    @Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender, SurveyRepository surveyRepository, ContactRepository contactRepository, SurveyContactConnectorRepository surveyContactRepository, UserRepository userRepository) {
        this.javaMailSender = javaMailSender;
        this.surveyRepository = surveyRepository;
        this.contactRepository = contactRepository;
        this.surveyContactRepository = surveyContactRepository;
        this.userRepository = userRepository;
    }

    public void sendEmailForUser(Long idUser, String surveyId, String[] emails) {
        User user = userRepository.findFirstById(idUser).orElseThrow(UserNotFound::new);
        for (String email : emails) {
            Contact contact = newContact(idUser, email);
            Survey survey = surveyRepository.findFirstByIdForNormPeople(Long.valueOf(surveyId)).orElseThrow(SurveyNotFound::new);
            newSurveyContact(survey, contact);
        }
        for (String email : emails) {
            String codEmail = email + ";" + surveyId;
            String encodedString = BASE_URL + END_POINT + Base64.getEncoder().withoutPadding().encodeToString(codEmail.getBytes());
            String message = "Message from " + user.getEmail() + ": " + "<<Please, follow the link and take the survey " + encodedString + " >>";
            sendMail(email, subject, message);
        }
    }

    private Optional<SurveyContact> surveyContact(Long contactId, Long surveyId) {
        return surveyContactRepository.findByContactAndSurvey(contactId, surveyId);
    }

    private void newSurveyContact(Survey survey, Contact contact) {
        if (surveyContact(contact.getId(), survey.getId()).isPresent()) {
            SurveyContact surveyContact = surveyContact(contact.getId(), survey.getId()).get();
            if (surveyContact.isCanPass()) {
                throw new SurveyAlreadyReceivedException(contact.getEmail() + " already received the survey");
            } else {
                throw new SurveyAlreadyPassedException(contact.getEmail() + " already passed the survey");
            }
        } else {
            SurveyContact surveyContact = new SurveyContact();
            surveyContact.setContact(contact);
            surveyContact.setSurvey(survey);
            surveyContact.setCanPass(true);
            surveyContactRepository.save(surveyContact);
        }
    }

    private boolean isContactExist(String email, Long userId) {
        return contactRepository.findByEmailAndUserId(email, userId).isPresent();
    }

    private Contact newContact(Long userId, String email) {
        Contact contact = new Contact();
        User user = userRepository.findFirstById(userId).orElseThrow(UserNotFound::new);
        if (isContactExist(email, userId)) {
            contact = contactRepository.findByEmailAndUserId(email, userId).orElseThrow(ContactNotFound::new);
        } else {
            contact.setEmail(email);
            contact.setUser(user);
            contactRepository.save(contact);
            return contact;
        }
        return contact;
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
