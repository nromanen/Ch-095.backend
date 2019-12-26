package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.SurveyStatisticDTO;

import java.util.Optional;

public interface StatisticService {

    Optional<SurveyStatisticDTO> getSurveyWithQuestionsAnswers(Long surveyId);
}
