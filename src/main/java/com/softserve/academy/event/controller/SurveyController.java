package com.softserve.academy.event.controller;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.response.ServerResponse;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ServerResponse<Page<SimpleSurveyDTO>> findAllSurveys(
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestBody(required = false) Map<String, Map<String, Object>> filters) {
        return ServerResponse.success(service.findAllFiltered(pageable, filters));
    }

    @ApiOperation(value = "Duplicates a survey")
    @PostMapping
    public ServerResponse<SimpleSurveyDTO> duplicateSurvey(Long id) {
        return ServerResponse.success(service.duplicateSurvey(id));
    }

    @ApiOperation(value = "Сhange the title of the survey")
    @PutMapping
    public ServerResponse<String> updateTitle(Long id, String title) {
        return ServerResponse.success(service.setTitleForSurvey(id, title));
    }

    @ApiOperation(value = "Delete a survey")
    @DeleteMapping
    public ServerResponse<Long> deleteSurvey(Survey survey) {
        service.delete(survey);
        return ServerResponse.success(survey.getId());
    }
}
