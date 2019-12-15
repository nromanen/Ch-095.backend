package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.response.ServerResponse;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @GetMapping
    public SurveyContactDTO startSurvey(@RequestParam Long surveyId, @RequestParam String contactEmail){
        List<SurveyQuestion> questions = questionService.findBySurveyId(surveyId);
        Collections.sort(questions);
        List<QuestionDTO> questionsDTO = questionMapper.listQuestionToDTO(questions);
        SurveyContactDTO dto = new SurveyContactDTO();
        dto.setContactEmail(contactEmail);
        dto.setSurveyId(surveyId);
        dto.setQuestions(questionsDTO);
        return dto;
    }
}