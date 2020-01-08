package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
import com.softserve.academy.event.exception.SurveyNotFound;

import java.util.Set;

public interface StatisticService {

    QuestionsGeneralStatisticDTO getGeneralStatistic(Long surveyId);

    Set<QuestionsSeparatelyStatisticDTO> getSeparatelyStatistic(Long surveyId);


    boolean isSurveyBelongsUser(Long surveyId) throws SurveyNotFound;
}
