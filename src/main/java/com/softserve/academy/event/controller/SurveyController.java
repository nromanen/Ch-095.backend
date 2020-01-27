package com.softserve.academy.event.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.EditSurveyDTO;
import com.softserve.academy.event.dto.SaveSurveyDTO;
import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.mapper.SaveQuestionMapper;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Api(value = "/survey")
@RestController
@RequestMapping("survey")
@Slf4j
public class SurveyController {

    private final SaveQuestionMapper saveQuestionMapper;
    private final SurveyService service;

    @Autowired
    public SurveyController(SurveyService service, SaveQuestionMapper saveQuestionMapper) {
        this.saveQuestionMapper = saveQuestionMapper;
        this.service = service;
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
        return ResponseEntity.ok(service.duplicate(settings));
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

    @ApiOperation(value = "Disable a survey")
    @PutMapping("/disable")
    public ResponseEntity<HttpStatus> disableSurvey(@RequestParam Long id) {
        service.disable(id);
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
        return ResponseEntity.ok(service.saveSurveyWithQuestions(saveSurveyDTO));
    }

    @ApiOperation(value = "Get a survey and get user access to edit him", response = SaveSurveyDTO.class)
    @GetMapping(value = "/edit/{id}")
    public ResponseEntity<EditSurveyDTO> loadForEditSurvey(@PathVariable(name = "id") Long surveyId) throws IOException {
        return service.loadSurvey(surveyId);
    }


    @ApiOperation(value = "Get a survey and get user access to edit him", response = SaveSurveyDTO.class)
    @PostMapping(value = "/update/{id}")
    public ResponseEntity updateSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO, @PathVariable("id") String id) throws
            JsonProcessingException {
        return ResponseEntity.ok(service.updateSurvey(Long.parseLong(id), saveSurveyDTO));
    }


}
