package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200/question")
@RequestMapping("question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionMapper questionMapper;

    @RequestMapping(method = RequestMethod.GET)
    public SurveyContactDTO startSurvey(Long surveyId, String contactEmail){
        List<QuestionDTO> questions = questionMapper.listQuestionToDTO(questionService.findBySurveyId(surveyId));
        SurveyContactDTO dto = new SurveyContactDTO();
        dto.setContactEmail(contactEmail);
        dto.setSurveyId(surveyId);
        dto.setQuestions(questions);
        return dto;
    }
}