package com.softserve.academy.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "/survey")
@RestController
@RequestMapping("survey")
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
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, name = "status") String status) {
        return ResponseEntity.ok(
                surveyMapper.pageToDTO(service.findAllByPageableAndStatus(pageable, status))
        );
    }

    @ApiOperation(value = "Duplicates a survey")
    @PostMapping
    public ResponseEntity<SurveyDTO> duplicateSurvey(@RequestBody DuplicateSurveySettings settings) {
        return ResponseEntity.ok(
                surveyMapper.toDTO(service.duplicateSurvey(settings))
        );
    }

    @ApiOperation(value = "Сhange the title of the survey")
    @PutMapping
    public ResponseEntity<HttpStatus> updateTitle(@RequestParam Long id, @RequestParam String title) {
        return ResponseEntity.ok(service.updateTitle(id, title));
    }

    @PutMapping("/status/active")
    public ResponseEntity<HttpStatus> setStatusActive(@RequestParam Long id) {
        return ResponseEntity.ok(service.updateStatus(id, SurveyStatus.ACTIVE));
    }

    @PutMapping("/status/done")
    public ResponseEntity<HttpStatus> setStatusDone(@RequestParam Long id) {
        return ResponseEntity.ok(service.updateStatus(id, SurveyStatus.DONE));
    }

    @ApiOperation(value = "Delete a survey")
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteSurvey(@RequestParam Long id) {
        service.delete(new Survey(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping(value = "/createNewSurvey")
    public ResponseEntity saveSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO) throws JsonProcessingException {
        Survey survey = new Survey();
        survey.setTitle(saveSurveyDTO.getTitle());
        survey.setImageUrl(saveSurveyDTO.getSurveyPhotoName());
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        for (SurveyQuestionDTO x : saveSurveyDTO.getQuestions()) {
            surveyQuestions.add(saveQuestionMapper.toEntity(x));
        }
        return ResponseEntity.ok(service.saveSurveyWithQuestions(survey, surveyQuestions));
    }


    @ApiOperation(value = "Get a survey and get user access to edit him", response = SaveSurveyDTO.class)
    @GetMapping(value = "/edit/{id}")
    public ResponseEntity loadForEditSurvey(@PathVariable(name = "id") Long surveyId) throws JsonProcessingException {
        List<SurveyQuestion> questions = questionService.findBySurveyId(surveyId);
        List<SurveyQuestionDTO> questionsDTO = new ArrayList<>();
        for (SurveyQuestion x : questions) {
            questionsDTO.add(saveQuestionMapper.toDTO(x));
        }
        SaveSurveyDTO saveSurveyDTO = new SaveSurveyDTO(questionsDTO);
        Survey survey = service.findFirstById(surveyId).get();
        saveSurveyDTO.setTitle(survey.getTitle());
        saveSurveyDTO.setSurveyPhotoName(survey.getImageUrl());
        if (questionsDTO.isEmpty())
            return new ResponseEntity(saveSurveyDTO, HttpStatus.BAD_REQUEST);
        return new ResponseEntity(saveSurveyDTO, HttpStatus.OK);
    }


    @ApiOperation(value = "Get a survey and get user access to edit him", response = SaveSurveyDTO.class)
    @PostMapping(value = "/update/{id}")
    public ResponseEntity updateSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO, @PathVariable("id") String id) throws JsonProcessingException {
        List<SurveyQuestion> questions = new ArrayList<>();
        for (SurveyQuestionDTO x : saveSurveyDTO.getQuestions()) {
            questions.add(saveQuestionMapper.toEntity(x));
        }
//        Long surveyId = Long.parseLong(editSurveyDTO.getSurveyId());
        return ResponseEntity.ok(service.editSurvey(Long.parseLong(id), questions));
//        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
