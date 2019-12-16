package com.softserve.academy.event.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.softserve.academy.event.dto.QuestionStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.exception.QuestionNotFoundException;
import com.softserve.academy.event.service.db.AnswerService;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.db.SurveyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/statistic")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class StatisticController {

    private QuestionService questionService;
    private AnswerService answerService;
    private SurveyService surveyService;

    public StatisticController(QuestionService questionService, AnswerService answerService, SurveyService surveyService) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.surveyService = surveyService;
    }

    @GetMapping("/questions")
    public List<QuestionStatisticDTO> getM(@RequestParam(name = "surveyId") long surveyId) {
        Survey survey = surveyService.findFirstById(surveyId).get();
        List<SurveyQuestion> questions = questionService.findBySurveyId(survey.getId());
        List<QuestionStatisticDTO> questionDTOS = questions.stream()
                .map(QuestionStatisticDTO::toSimpleQuestionDTO)
                .collect(Collectors.toList());
        return questionDTOS;
    }

    @GetMapping("/answers")
    public ResponseEntity<Map<String, Object>> getB(
            @RequestParam(name = "questionId") long questionId) {
        try {
            Map<String,Integer> statisticMap =
                    answerService.createStatisticAnswersMap(questionId);
            Map<String,Object> map = new HashMap<>();
            map.put("data",statisticMap.values());
            map.put("labels",statisticMap.keySet());
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (QuestionNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}