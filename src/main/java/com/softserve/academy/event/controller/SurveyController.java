package com.softserve.academy.event.controller;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.SaveSurveyDTO;
import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.response.ServerResponse;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("survey")
public class SurveyController {

    private final SurveyService service;

    public SurveyController(SurveyService service) {
        this.service = service;
    }

    @GetMapping
    public ServerResponse<Page<SimpleSurveyDTO>> findAllSurveys(
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestBody(required = false) Map<String, Map<String, Object>> filters) {
        return ServerResponse.success(service.findAllFiltered(pageable, filters));
    }

    @PostMapping
    public ServerResponse<SimpleSurveyDTO> duplicateSurvey(Long id) {
        return ServerResponse.success(service.duplicateSurvey(id));
    }

    @PutMapping
    public ServerResponse<String> updateTitle(Long id, String title) {
        return ServerResponse.success(service.setTitleForSurvey(id, title));
    }

    @DeleteMapping
    public ServerResponse<Long> deleteSurvey(Survey survey) {
        service.delete(survey);
        return ServerResponse.success(survey.getId());
    }

    @PostMapping(value = "/createNewSurvey")
    public ServerResponse<Survey> saveSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO) {
        Survey survey = new Survey();
        survey.setTitle(saveSurveyDTO.getTitle());
        long userID = saveSurveyDTO.getUserID();
        List<SurveyQuestion> surveyQuestions = saveSurveyDTO.questionsDtoToEntity(survey);
        return ServerResponse.success(service.saveSurveyWithQuestions(survey, userID, surveyQuestions));
    }
}
