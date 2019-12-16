package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.ContactResponseDTO;
import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.entity.SurveyContactConnector;
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

@Api(value = "/question")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("question")
public class QuestionController {

    private final ContactService contactService;
    private final QuestionService questionService;
    private final QuestionMapper questionMapper;
    private final AnswerService answerService;
    private final SurveyContactConnectorService sccService;
    private final AnswerMapper answerMapper;

    @Autowired
    public QuestionController(AnswerMapper answerMapper, ContactService contactService, QuestionService questionService, QuestionMapper questionMapper, AnswerService answerService, SurveyContactConnectorService sccService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.sccService = sccService;
        this.contactService = contactService;
        this.questionMapper = questionMapper;
        this.answerMapper = answerMapper;
    }

    @ApiOperation(value = "Get a survey form for contact", notes = "Checks e-mail, surveyId and build form", response = SurveyContactDTO.class)
    @GetMapping
    public ResponseEntity<SurveyContactDTO> startSurvey(@RequestParam Long surveyId, @RequestParam String contactEmail){
        List<SurveyQuestion> questions = questionService.findBySurveyId(surveyId);
        List<QuestionDTO> questionsDTO = questionMapper.listQuestionToDTO(questions);
        SurveyContactDTO dto = new SurveyContactDTO();
        dto.setContactEmail(contactEmail);
        dto.setSurveyId(surveyId);
        dto.setQuestions(questionsDTO);
        if(questionsDTO.isEmpty())
            return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @ApiOperation(value = "Save answers to the database")
    @PostMapping
    public ResponseEntity<String> addAnswers(ContactResponseDTO contactResponseDTO){
        String result;
        Long contactId =
                contactService.getIdByEmail(contactResponseDTO.getContactEmail()).orElse(null);
        if(contactId == null) {
            result = "missing email data";
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        SurveyContactConnector scc =
                sccService.findByContactAndSurvey(contactId, contactResponseDTO.getSurveyId()).orElse(null);
        if(scc == null){
            result = "mail is not in the invite list";
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        scc.setEnable(true);
        sccService.update(scc);
        contactResponseDTO.getAnswers().stream()
                .map(answerMapper::toEntity)
                .forEach(answerService::save);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}