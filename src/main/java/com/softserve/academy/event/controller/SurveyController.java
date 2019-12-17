package com.softserve.academy.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.SaveSurveyDTO;
import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.dto.SurveyQuestionDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.mapper.SaveQuestionMapper;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("survey")
@CrossOrigin(origins = "http://localhost:4200")
public class SurveyController {

    private final SaveQuestionMapper saveQuestionMapper;
    private final SurveyService service;

    @Autowired
    public SurveyController(SaveQuestionMapper saveQuestionMapper, SurveyService service) {
        this.saveQuestionMapper = saveQuestionMapper;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<SimpleSurveyDTO>> findAllSurveys(
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestBody(required = false) Map<String, Map<String, Object>> filters) {
        return ResponseEntity.ok(service.findAllFiltered(pageable, filters));
    }

    @PostMapping
    public ResponseEntity<SimpleSurveyDTO> duplicateSurvey(Long id) {
        return ResponseEntity.ok(service.duplicateSurvey(id));
    }

    @PutMapping
    public ResponseEntity<String> updateTitle(Long id, String title) {
        return ResponseEntity.ok(service.setTitleForSurvey(id, title));
    }

    @DeleteMapping
    public ResponseEntity<Long> deleteSurvey(Survey survey) {
        service.delete(survey);
        return ResponseEntity.ok(survey.getId());
    }

    @PostMapping(value = "/createNewSurvey")
    public ResponseEntity<Survey> saveSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO) throws JsonProcessingException {
        Survey survey = new Survey();
        survey.setTitle(saveSurveyDTO.getTitle());
        long userID = saveSurveyDTO.getUserID();
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for(SurveyQuestionDTO surveyQuestionDTO : saveSurveyDTO.getQuestions()){
            SurveyQuestion surveyQuestion = saveQuestionMapper.toEntity(surveyQuestionDTO);
            String  answers =  mapper.writeValueAsString(surveyQuestionDTO.getAnswers());
            surveyQuestion.setAnswers(answers);
            surveyQuestions.add(surveyQuestion);
        }
        return ResponseEntity.ok(service.saveSurveyWithQuestions(survey, userID, surveyQuestions));
    }
}
