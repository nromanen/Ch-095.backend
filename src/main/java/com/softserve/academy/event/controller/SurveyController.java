package com.softserve.academy.event.controller;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.mapper.SurveyMapper;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("survey")
@CrossOrigin(origins = "http://localhost:4200")
public class SurveyController {

    private final SurveyService service;
    private final SurveyMapper surveyMapper;

    public SurveyController(SurveyService service, SurveyMapper surveyMapper) {
        this.service = service;
        this.surveyMapper = surveyMapper;
    }

    @GetMapping
    public ResponseEntity<Page<SurveyDTO>> findAllSurveys(
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestBody(required = false) Map<String, Map<String, Object>> filters) {
        return ResponseEntity.ok(surveyMapper.pageToDTO(service.findAllFiltered(pageable, filters)));
    }

    @PostMapping
    public ResponseEntity<SurveyDTO> duplicateSurvey(@RequestBody DuplicateSurveySettings settings) {
        return ResponseEntity.ok(surveyMapper.toDTO(service.duplicateSurvey(settings)));
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

}
