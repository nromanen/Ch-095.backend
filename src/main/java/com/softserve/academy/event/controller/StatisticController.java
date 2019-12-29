package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.SurveyEachStatisticDTO;
import com.softserve.academy.event.dto.SurveyGeneralStatisticDTO;
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

    @GetMapping("/general")
    public ResponseEntity<SurveyGeneralStatisticDTO> getGeneralStatistic(
            @RequestParam(name = "surveyId") long surveyId)  {
        log.info("call with surveyId = " + surveyId);
        Optional<SurveyGeneralStatisticDTO> surveyDTO= statisticService.getGeneralStatistic(surveyId);
        return surveyDTO.map(value -> {
            log.info("return " + HttpStatus.OK +"for surveyId = " + surveyId);
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            log.info("return " + HttpStatus.BAD_REQUEST +"for surveyId = " + surveyId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;
        });

    }

    @GetMapping("/each")
    public ResponseEntity<SurveyEachStatisticDTO> getQuestions(
            @RequestParam(name = "surveyId") long surveyId)  {
        log.info("call with surveyId = " + surveyId);
        Optional<SurveyEachStatisticDTO> surveyDTO= statisticService.getEachStatistic(surveyId);
        return surveyDTO.map(value -> {
            log.info("return " + HttpStatus.OK +"for surveyId = " + surveyId);
            return new ResponseEntity<>(value, HttpStatus.OK);
        }).orElseGet(() -> {
            log.info("return " + HttpStatus.BAD_REQUEST +"for surveyId = " + surveyId);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST) ;
        });

    }
}