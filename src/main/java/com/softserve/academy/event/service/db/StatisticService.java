package com.softserve.academy.event.service.db;

import com.softserve.academy.event.dto.SurveyEachStatisticDTO;
import com.softserve.academy.event.dto.SurveyGeneralStatisticDTO;

import java.util.Optional;

public interface StatisticService {

    Optional<SurveyGeneralStatisticDTO> getGeneralStatistic(Long surveyId);

    Optional<SurveyEachStatisticDTO> getEachStatistic(Long surveyId);

}
