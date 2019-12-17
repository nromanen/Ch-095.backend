package com.softserve.academy.event.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.QuestionStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.exception.QuestionNotFoundException;
import com.softserve.academy.event.service.db.AnswerService;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/statistic")
@Slf4j
public class StatisticController {

    private QuestionService questionService;
    private AnswerService answerService;
    private SurveyService surveyService;
    private QuestionMapper questionMapper;

    public StatisticController(QuestionService questionService,
                               AnswerService answerService,
                               SurveyService surveyService, QuestionMapper questionMapper) {
        this.questionService = questionService;
        this.answerService = answerService;
        this.surveyService = surveyService;
        this.questionMapper = questionMapper;
    }

    @CrossOrigin(origins = "http://localhost:4200/statistic/questions")
    @GetMapping("/questions")
    public ResponseEntity<List<QuestionStatisticDTO>> getQuestions(
            @RequestParam(name = "surveyId") long surveyId) {
        log.info("Called getQuestions with surveyId = " + surveyId );
        Optional<Survey> surveyOptional = surveyService.findFirstById(surveyId);
        if(surveyOptional.isPresent()) {
            Survey survey = surveyOptional.get();
            List<SurveyQuestion> questions = questionService.findBySurveyId(survey.getId());
            List<QuestionStatisticDTO> questionDTOS =
                    questionMapper.listQuestionToStatisticDTO(questions);
            log.info("Return responseEntity for surveyId = "
                    + surveyId + " with HttpStatus" + HttpStatus.OK);
            return new ResponseEntity<>(questionDTOS,HttpStatus.OK);
        }
        else {
            log.info("Return responseEntity for surveyId = "
                    + surveyId + " with " + HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200/statistic/answers")
    @GetMapping("/answers")
    public ResponseEntity<Map<String, Object>> getAnswers(
            @RequestParam(name = "questionId") long questionId) {
        try {
            log.info("Called getAnswers with questionId = " + questionId );
            Map<String,Integer> statisticMap =
                    answerService.createStatisticAnswersMap(questionId);
            Map<String,Object> map = new HashMap<>();
            map.put("data",statisticMap.values());
            map.put("labels",statisticMap.keySet());
            log.info("Return responseEntity for questionId = "
                    + questionId + " with HttpStatus " + HttpStatus.OK);
            return new ResponseEntity<>(map,HttpStatus.OK);
        }
        catch (JsonProcessingException e) {
            log.error(e.toString());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        catch (QuestionNotFoundException e) {
            log.info("Return responseEntity for questionId = "
                    + questionId + " with HttpStatus" + HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin(origins = "http://localhost:4200/statistic/surveyTitle")
    @GetMapping("/surveyTitle")
    public ResponseEntity<String> getSurveyTitle(
            @RequestParam(name = "surveyId") long surveyId){
        log.info("Called getSurveyTitle with surveyId = " + surveyId );
        Optional<Survey> surveyOptional = surveyService.findFirstById(surveyId);
        if(surveyOptional.isPresent()){
            log.info("Return responseEntity for surveyId = "
                    + surveyId + " with HttpStatus " + HttpStatus.OK);
            return new ResponseEntity<>(surveyOptional.get().getTitle(),HttpStatus.OK);
        }
        else {
            log.info("Return responseEntity for surveyId = "
                    + surveyId + " with HttpStatus " + HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}