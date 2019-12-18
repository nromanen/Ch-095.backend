package com.softserve.academy.event.service.db;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.entity.SurveyAnswer;

import java.util.Map;

public interface AnswerService extends BasicService<SurveyAnswer, Long> {

    Map<String, Integer> createStatisticAnswersMap(Long questionId) throws JsonProcessingException;
}
