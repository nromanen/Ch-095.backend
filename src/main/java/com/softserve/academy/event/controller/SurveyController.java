package com.softserve.academy.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.*;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.mapper.SaveQuestionMapper;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Api(value = "/survey")
@RestController
@RequestMapping("survey")
@Slf4j
@PropertySource("classpath:application.properties")
public class SurveyController {

    @Value("${image.upload.dir}")
    private String imageUploadDir;

    private final SaveQuestionMapper saveQuestionMapper;
    private final SurveyService service;
    private final QuestionService questionService;

    @Autowired
    public SurveyController(SurveyService service,
                            SaveQuestionMapper saveQuestionMapper, QuestionService questionService) {
        this.saveQuestionMapper = saveQuestionMapper;
        this.service = service;
        this.questionService = questionService;
    }

    @ApiOperation(value = "Get all surveys")
    @GetMapping
    public ResponseEntity<Page<SurveyDTO>> findAllSurveys(
            Pageable pageable,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(
                service.findAllByPageableAndStatus(pageable, status)
        );
    }

    @ApiOperation(value = "Duplicates a survey")
    @PostMapping
    public ResponseEntity<Long> duplicateSurvey(@RequestBody DuplicateSurveySettings settings) {
        return ResponseEntity.ok(
                service.duplicateSurvey(settings)
        );
    }

    @ApiOperation(value = "Change the title of the survey")
    @PutMapping
    public ResponseEntity<HttpStatus> updateTitle(@RequestParam Long id, @RequestParam String title) {
        service.updateTitle(id, title);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/status/{status}")
    public ResponseEntity<HttpStatus> setStatusDone(@RequestParam Long id, @PathVariable SurveyStatus status) {
        service.updateStatus(id, status);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a survey")
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteSurvey(@RequestParam Long id) {
        service.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping(value = "/createNewSurvey")
    public ResponseEntity saveSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO) throws IOException {
        Survey survey = saveQuestionMapper.toSurvey(saveSurveyDTO);
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        for (SurveyQuestionDTO question : saveSurveyDTO.getQuestions()) {
            surveyQuestions.add(saveQuestionMapper.toEntity(question));
        }
        return ResponseEntity.ok(service.saveSurveyWithQuestions(survey, surveyQuestions));
    }

    @ApiOperation(value = "Get a survey and get user access to edit him", response = SaveSurveyDTO.class)
    @GetMapping(value = "/edit/{id}")
    public ResponseEntity<EditSurveyDTO> loadForEditSurvey(@PathVariable(name = "id") Long surveyId) throws IOException {
        List<SurveyQuestion> questions = questionService.findBySurveyId(surveyId);
        List<EditSurveyQuestionDTO> editSurveyQuestionsDTO = new ArrayList<>();
        for (SurveyQuestion question : questions) {
            EditSurveyQuestionDTO editSurveyQuestionDTO = saveQuestionMapper.toEditSurveyQuestionDTO(question);
            if(question.getType().equals(SurveyQuestionType.CHECKBOX_PICTURE) || question.getType().equals(SurveyQuestionType.RADIO_PICTURE)){
                for(String filename : editSurveyQuestionDTO.getChoiceAnswers()){
                    editSurveyQuestionDTO.getUploadingPhotos().add(getPhotoAsStrByFilename(filename));
                }
            }
            editSurveyQuestionsDTO.add(editSurveyQuestionDTO);
        }
        Survey survey = service.findFirstById(surveyId).orElseThrow(SurveyNotFound::new);
        EditSurveyDTO editSurveyDTO = saveQuestionMapper.toEditSurveyDTO(survey, editSurveyQuestionsDTO);
        return new ResponseEntity<>(editSurveyDTO, HttpStatus.OK);
    }

    private String getPhotoAsStrByFilename(String filename) throws IOException {
        Path path = Paths.get(imageUploadDir,filename);
        byte[] arr = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(arr);
    }


    @ApiOperation(value = "Get a survey and get user access to edit him", response = SaveSurveyDTO.class)
    @PostMapping(value = "/update/{id}")
    public ResponseEntity updateSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO, @PathVariable("id") String id) throws
            JsonProcessingException {
        List<SurveyQuestion> questions = new ArrayList<>();
        for (SurveyQuestionDTO questionDTO : saveSurveyDTO.getQuestions()) {
            questions.add(saveQuestionMapper.toEntity(questionDTO));
        }
        return ResponseEntity.ok(service.updateSurvey(Long.parseLong(id), questions));
    }
}
