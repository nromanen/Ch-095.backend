package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.ContactResponseDTO;
import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.service.db.AnswerService;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import com.softserve.academy.event.service.mapper.AnswerMapper;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "/question")
@RestController
@RequestMapping("question")
public class QuestionController {

    private final ContactService contactService;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final AnswerService answerService;
    private final SurveyContactConnectorService surveyContactConnectorService;
    private final AnswerMapper answerMapper;

    @Autowired
    public QuestionController(AnswerMapper answerMapper, ContactService contactService, QuestionService questionService, QuestionMapper questionMapper, AnswerService answerService, SurveyContactConnectorService surveyContactConnectorService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.surveyContactConnectorService = surveyContactConnectorService;
        this.contactService = contactService;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
    }

    @ApiOperation(value = "Get a survey form for contact", notes = "Checks an e-mail, Id(survey) and builds a form", response = SurveyContactDTO.class)
    @GetMapping
    public ResponseEntity<SurveyContactDTO> startSurvey(Long surveyId, String contactEmail){
        if (!contactService.canPass(surveyId, contactEmail))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        List<SurveyQuestion> questions = questionService.findBySurveyId(surveyId);
        List<QuestionDTO> questionsDTO = questionMapper.listQuestionToDTO(questions);
        if(questionsDTO.isEmpty())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        SurveyContactDTO dto = new SurveyContactDTO(surveyId, contactEmail, questionsDTO);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ApiOperation(value = "Save answers to the database")
    @PostMapping
    public ResponseEntity<String> addAnswers(@RequestBody ContactResponseDTO contactResponseDTO){
        Long contactId = contactService.getIdByEmail(contactResponseDTO.getContactEmail());
        SurveyContact surveyContact = surveyContactConnectorService.findByContactAndSurvey(contactId, contactResponseDTO.getSurveyId());
        if(contactId == null || surveyContact == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        surveyContact.setCanPass(false);
        surveyContactConnectorService.update(surveyContact);
        List<SurveyAnswer> answers = contactResponseDTO.getAnswers().stream()
                .peek(answerDTO -> answerDTO.setContactId(contactId))
                .map(answerMapper::toEntity)
                .collect(Collectors.toList());
        answerService.saveAll(answers);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
