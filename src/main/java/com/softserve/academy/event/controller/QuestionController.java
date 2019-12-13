package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @RequestMapping(method = RequestMethod.GET)
    public SurveyContactDTO startSurvey(@RequestParam Long surveyId, @RequestParam String contactEmail){
        SurveyContactDTO dto = new SurveyContactDTO();
        dto.setContactEmail(contactEmail);
        dto.setSurveyId(surveyId);
        dto.setQuestions(questionMapper.listQuestionToDTO(questionService.findBySurveyId(surveyId)));
        return dto;
    }
}