package com.softserve.academy.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.*;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.mapper.SaveQuestionMapper;
import com.softserve.academy.event.service.mapper.SurveyMapper;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "/survey")
@RestController
@RequestMapping("survey")
@Slf4j
public class SurveyController {

    private final QuestionService questionService;
    private final SurveyService service;
    private final SaveQuestionMapper saveQuestionMapper;
    private final SurveyMapper surveyMapper;

    @Autowired
    public SurveyController(QuestionService questionService, SurveyService service, SurveyMapper surveyMapper, SaveQuestionMapper saveQuestionMapper) {
        this.questionService = questionService;
        this.saveQuestionMapper = saveQuestionMapper;
        this.service = service;
        this.surveyMapper = surveyMapper;
    }

    @ApiOperation(value = "Get all surveys")
    @GetMapping
    public ResponseEntity<Page<SurveyDTO>> findAllSurveys(
            @PageableDefault(sort = "creationDate", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, name = "status") String status) {
        return ResponseEntity.ok(
                service.findAllByPageableAndStatus(pageable, status)
        );
    }

    @ApiOperation(value = "Duplicates a survey")
    @PostMapping
    public ResponseEntity<SurveyDTO> duplicateSurvey(@RequestBody DuplicateSurveySettings settings) {
        return ResponseEntity.ok(
                surveyMapper.toDTO(service.duplicateSurvey(settings))
        );
    }

    @ApiOperation(value = "Change the title of the survey")
    @PutMapping
    public ResponseEntity<Boolean> updateTitle(@RequestParam Long id, @RequestParam String title) {
        service.updateTitle(id, title);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/status/active")
    public ResponseEntity<Boolean> setStatusActive(@RequestParam Long id) {
        service.updateStatus(id, SurveyStatus.ACTIVE);
        return ResponseEntity.ok(true);
    }

    @PutMapping("/status/done")
    public ResponseEntity<Boolean> setStatusDone(@RequestParam Long id) {
        service.updateStatus(id, SurveyStatus.DONE);
        return ResponseEntity.ok(true);
    }

    @ApiOperation(value = "Delete a survey")
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteSurvey(@RequestParam Long id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping(value = "/createNewSurvey")
    public ResponseEntity saveSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO) throws JsonProcessingException {
            Survey survey = new Survey();
            survey.setTitle(saveSurveyDTO.getTitle());
            survey.setImageUrl(saveSurveyDTO.getSurveyPhotoName());
            List<SurveyQuestion> surveyQuestions = getQuestionsEntities(saveSurveyDTO.getQuestions());
            return ResponseEntity.ok(service.saveSurveyWithQuestions(survey, surveyQuestions));
    }

    /**
      Method gets list of Question DTO and made list of entities with correct variant of answers
      Mapper can't make string from list, so i set it through object mapper
      @return List<SurveyQuestion> - list of entities but without established survey
    */
    private List<SurveyQuestion> getQuestionsEntities(List<SurveyQuestionDTO> surveyQuestionsDTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        for (SurveyQuestionDTO surveyQuestionDTO : surveyQuestionsDTO) {
            SurveyQuestion surveyQuestion = saveQuestionMapper.toEntity(surveyQuestionDTO);
            String answers = mapper.writeValueAsString(surveyQuestionDTO.getAnswers());
            surveyQuestion.setAnswers(answers);
            surveyQuestions.add(surveyQuestion);
        }
        return surveyQuestions;
    }

}
