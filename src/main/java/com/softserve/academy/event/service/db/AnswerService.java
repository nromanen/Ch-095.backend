package com.softserve.academy.event.service.db;

import com.softserve.academy.event.entity.SurveyAnswer;

import java.util.List;

public interface AnswerService extends BasicService<SurveyAnswer, Long> {
    List<SurveyAnswer> saveAll(List<SurveyAnswer> answers);
}
