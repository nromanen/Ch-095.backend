package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.SurveyStatisticDTO;
import com.softserve.academy.event.service.db.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/statistic")
@Slf4j
public class StatisticController {

    private StatisticService statisticService;

    @Autowired
    public void setStatisticService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    public ResponseEntity<SurveyStatisticDTO> getQuestions(
            @RequestParam(name = "surveyId") long surveyId)  {
        log.info("call with surveyId = " + surveyId);
        Optional<SurveyStatisticDTO> surveyDTO= statisticService.getSurveyWithQuestionsAnswers(surveyId);
        return surveyDTO.map(value -> {
            log.info("return " + HttpStatus.OK +"for surveyId = " + surveyId);
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            log.info("return " + HttpStatus.BAD_REQUEST +"for surveyId = " + surveyId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;
        });

    }
}