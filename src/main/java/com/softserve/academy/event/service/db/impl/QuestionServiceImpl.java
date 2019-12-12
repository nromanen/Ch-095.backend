package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.service.db.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl extends BasicServiceImpl<SurveyQuestion, Long> implements QuestionService{

    @Autowired
    QuestionRepository questionRepository;

    @Override
    public List<SurveyQuestion> findBySurveyId(Long surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }
}
