package com.softserve.academy.event.controller;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(value = "/survey")
@RestController
@RequestMapping("survey")
public class SurveyController {

    private final SurveyService service;

    @Autowired
    public SurveyController(SurveyService service) {
        this.service = service;
    }

    @ApiOperation(value = "Get all surveys")
    @GetMapping
    public ResponseEntity<Page<SimpleSurveyDTO>> findAllSurveys(
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestBody(required = false) Map<String, Map<String, Object>> filters) {
        return ResponseEntity.ok(service.findAllFiltered(pageable, filters));
    }

    @ApiOperation(value = "Duplicates a survey")
    @PostMapping
    public ResponseEntity<SimpleSurveyDTO> duplicateSurvey(Long id) {
        return ResponseEntity.ok(service.duplicateSurvey(id));
    }

    @ApiOperation(value = "Сhange the title of the survey")
    @PutMapping
    public ResponseEntity<String> updateTitle(Long id, String title) {
        return ResponseEntity.ok(service.setTitleForSurvey(id, title));
    }

    @ApiOperation(value = "Delete a survey")
    @DeleteMapping
    public ResponseEntity<Long> deleteSurvey(Survey survey) {
        service.delete(survey);
        return ResponseEntity.ok(survey.getId());
    }
}
