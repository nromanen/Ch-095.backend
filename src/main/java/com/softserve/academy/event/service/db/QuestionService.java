package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.*;

import java.util.List;

public interface QuestionService extends BasicService<SurveyQuestion, Long>{

    List<SurveyQuestion> findBySurveyId(Long surveyId);

    List<SurveyAnswer> saveAnswers(List<SurveyAnswer> answers);

    List<SurveyAnswer> saveAnswers(List<SurveyAnswer> answers, SurveyContact surveyContact);

}
