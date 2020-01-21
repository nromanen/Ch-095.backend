package com.softserve.academy.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.ContactResponseDTO;
import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.entity.Respondent;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.service.db.*;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import com.softserve.academy.event.service.db.AnswerService;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import com.softserve.academy.event.service.mapper.AnswerMapper;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Api(value = "/question")
@RestController
@RequestMapping("question")
@PropertySource("classpath:application.properties")
public class QuestionController {

    @Value("${image.upload.dir}")
    private String imageUploadDir;

    private final ContactService contactService;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final AnswerService answerService;
    private final SurveyContactConnectorService surveyContactConnectorService;
    private final AnswerMapper answerMapper;
    private final AnonymService anonymService;
    private final RespondentService respondentService;

    @Autowired
    public QuestionController(AnswerMapper answerMapper, ContactService contactService, QuestionService questionService, QuestionMapper questionMapper, AnswerService answerService, SurveyContactConnectorService surveyContactConnectorService, AnonymService anonymService, RespondentService respondentService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.surveyContactConnectorService = surveyContactConnectorService;
        this.contactService = contactService;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
        this.anonymService = anonymService;
        this.respondentService = respondentService;
    }

    @ApiOperation(value = "Get a survey form for contact", notes = "Checks an e-mail, Id(survey) and builds a form", response = SurveyContactDTO.class)
    @GetMapping
    public ResponseEntity<SurveyContactDTO> startSurvey(Long surveyId, String contactEmail) {
        if (!contactService.canPass(surveyId, contactEmail))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return getSurveyContactDTO(surveyId, contactEmail);
    }

    @GetMapping(value = "common/{token}")
    public ResponseEntity<SurveyContactDTO> startSurvey(@PathVariable(name = "token") String token) {
        String[] strings = new String(Base64.getDecoder().decode(token)).split("~");
        Long id = Long.valueOf(strings[0]);

        return getSurveyContactDTO(id, "anonymousUser");
    }

    private ResponseEntity<SurveyContactDTO> getSurveyContactDTO(Long id, String respondent) {
        List<QuestionDTO> questionsDTO = questionMapper.listQuestionToDTO(questionService.findBySurveyId(id));
        savePhotoInQuestionDTO(questionsDTO);
        SurveyContactDTO dto = new SurveyContactDTO();
        dto.setContactEmail(respondent);
        dto.setSurveyId(id);
        dto.setQuestions(questionsDTO);
        if (questionsDTO.isEmpty())
            return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    private void  savePhotoInQuestionDTO(List<QuestionDTO> questionsDTO) throws IOException {
        for(QuestionDTO questionDTO : questionsDTO){
            if(questionDTO.getType().equals(SurveyQuestionType.RADIO_PICTURE) ||
              questionDTO.getType().equals(SurveyQuestionType.CHECKBOX_PICTURE)){
                for (String filename : new ObjectMapper().readValue(questionDTO.getChoiceAnswers(),String[].class)) {
                    questionDTO.getUploadingPhotos().add(FileUploadController.getPhotoAsEncodeStrByFilename(imageUploadDir,filename));
                }
            }
        }
    }

    @ApiOperation(value = "Save answers to the database")
    @PostMapping
    public ResponseEntity<String> addAnswers(@RequestBody ContactResponseDTO contactResponseDTO) {
        String result;
        Respondent respondent = null;
        Optional<Long> contactId = contactService.getIdByEmail(contactResponseDTO.getContactEmail());
        if (!contactId.isPresent()) {
            respondent = respondentService.save(
                    anonymService.save(
                            contactResponseDTO.getContactEmail()
                    )
            );
        }
        if (contactId.isPresent()) {
            Optional<SurveyContact> surveyContact =
                    surveyContactConnectorService.findByContactAndSurvey(contactId.get(), contactResponseDTO.getSurveyId());
            if (!surveyContact.isPresent()) {
                respondent = respondentService.save(
                        anonymService.save(
                                contactResponseDTO.getContactEmail()
                        )
                );
            }
            surveyContact.get().setCanPass(false);
            surveyContactConnectorService.update(surveyContact.get());
        }
        if (respondent == null) {
            respondent = respondentService.save(contactService.findFirstById(contactId.get()).get());
        }

        final Respondent finalRespondent = respondent;
        contactResponseDTO.getAnswers().stream()
                .map(answerMapper::toEntity)
                .forEach(surveyAnswer -> answerService.save(
                        new SurveyAnswer(null, surveyAnswer.getQuestion(),
                                finalRespondent, surveyAnswer.getValue())
                ));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
