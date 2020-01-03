package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.AnswerSurveyTitle;
import com.softserve.academy.event.dto.EmailDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.exception.IncorrectEmailsException;
import com.softserve.academy.event.service.db.EmailService;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SendEmailController {
    private final SurveyService surveyService;
    private final EmailService emailService;

    public SendEmailController(SurveyService surveyService, EmailService emailService) {
        this.surveyService = surveyService;
        this.emailService = emailService;
    }

    @GetMapping("/surveyTitle")
    @ResponseBody
    public ResponseEntity<AnswerSurveyTitle> getSurveyTitle(
            @RequestParam(name = "surveyId") String surveyId) {
        Optional<Survey> surveyOptional = surveyService.findFirstById(Long.parseLong(surveyId));
        return surveyOptional.map(survey -> new ResponseEntity<>(new AnswerSurveyTitle(
                survey.getTitle()), HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
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