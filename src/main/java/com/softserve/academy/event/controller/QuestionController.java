package com.softserve.academy.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.ContactResponseDTO;
import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.enums.SurveyType;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import com.softserve.academy.event.service.db.*;
import com.softserve.academy.event.service.mapper.AnswerMapper;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

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
    private final SurveyContactConnectorService surveyContactConnectorService;
    private final AnswerMapper answerMapper;
    private final SurveyService surveyService;

    @Autowired
    public QuestionController(AnswerMapper answerMapper, ContactService contactService, QuestionService questionService, QuestionMapper questionMapper, SurveyContactConnectorService surveyContactConnectorService, SurveyService surveyService) {
        this.questionService = questionService;
        this.surveyContactConnectorService = surveyContactConnectorService;
        this.contactService = contactService;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
        this.surveyService = surveyService;
    }

    @ApiOperation(value = "Get a survey form for contact", notes = "Checks an e-mail, Id(survey) and builds a form", response = SurveyContactDTO.class)
    @GetMapping
    public ResponseEntity<SurveyContactDTO> startSurvey(Long surveyId, String contactEmail) throws IOException {
        if (!contactService.canPass(surveyId, contactEmail))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return getSurveyContactDTO(surveyId, contactEmail);
    }

    @GetMapping(value = "common/{token}")
    public ResponseEntity<SurveyContactDTO> startSurvey(@PathVariable(name = "token") String token) throws IOException {
        String[] strings = new String(Base64.getDecoder().decode(token)).split("~");
        Long id = Long.valueOf(strings[0]);

        return getSurveyContactDTO(id, "anonymousUser");
    }

    private ResponseEntity<SurveyContactDTO> getSurveyContactDTO(Long id, String respondent) throws IOException {
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
    public ResponseEntity<String> addAnswers(@RequestBody ContactResponseDTO contactResponseDTO)
            throws NotFoundException, SurveyAlreadyPassedException {
        List<SurveyAnswer> answers = new LinkedList<>();
                contactResponseDTO.getAnswers().stream()
                .map(answerMapper::toEntity)
                .forEach(surveyAnswer -> answers.add(new SurveyAnswer(null, surveyAnswer.getQuestion(),
                                null, surveyAnswer.getValue()))
                );

        SurveyType type = surveyService.findFirstByIdForNormPeople(contactResponseDTO.getSurveyId())
                .orElseThrow(() -> new NotFoundException("Survey not found")).getType();

        if (type.equals(SurveyType.COMMON)) {
            questionService.saveAnswers(answers);
        } else if (type.equals(SurveyType.INDIVIDUAL)) {
            Long contactId = contactService.getIdByEmail(contactResponseDTO.getContactEmail())
                    .orElseThrow(() -> new NotFoundException("contact not found"));
            SurveyContact surveyContact = surveyContactConnectorService
                    .findByContactAndSurvey(contactId, contactResponseDTO.getSurveyId())
                    .orElseThrow(() -> new NotFoundException("SurveyContact not found"));
            if (!surveyContact.isCanPass()){
                throw new SurveyAlreadyPassedException("Survey already passed");
            } else {
                questionService.saveAnswers(answers, surveyContact);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
