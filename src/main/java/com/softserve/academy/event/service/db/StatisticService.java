package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
import com.softserve.academy.event.exception.SurveyNotFound;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

public interface StatisticService {

    Optional<QuestionsGeneralStatisticDTO> getGeneralStatistic(Long surveyId);

    Optional<Set<QuestionsSeparatelyStatisticDTO>> getSeparatelyStatistic(Long surveyId);


    boolean isSurveyBelongsUser(Long surveyId) throws SurveyNotFound;
}
