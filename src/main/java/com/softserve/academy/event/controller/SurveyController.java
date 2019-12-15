package com.softserve.academy.event.controller;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.mapper.SurveyMapper;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import org.springframework.http.HttpStatus;
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
            @RequestBody(required = false) Map<String, Map<String, Object>> filters) { /* todo OMFG GET haven't have body ... */
        return ResponseEntity.ok(
                surveyMapper.pageToDTO(service.findAllFiltered(pageable, filters))
        );
    }

    @PostMapping
    public ResponseEntity<SurveyDTO> duplicateSurvey(@RequestBody DuplicateSurveySettings settings) {
        return ResponseEntity.ok(
                surveyMapper.toDTO(service.duplicateSurvey(settings))
        );
    }

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

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteSurvey(@RequestParam Long id) {
        service.delete(new Survey(id));
        return ResponseEntity.ok(HttpStatus.OK);
    }

}
