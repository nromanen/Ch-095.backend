package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SurveyStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.service.db.StatisticService;
import com.softserve.academy.event.service.mapper.StatisticMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {

    private SurveyRepository surveyRepository;
    private StatisticMapper statisticMapper;

    public StatisticServiceImpl(SurveyRepository surveyRepository, StatisticMapper statisticMapper) {
        this.surveyRepository = surveyRepository;
        this.statisticMapper = statisticMapper;
    }

    @Override
    @Transactional
    public Optional<SurveyStatisticDTO> getSurveyWithQuestionsAnswers(Long id) {
        log.info("call with id = " + id);
        Optional<Survey> surveyOptional = surveyRepository.findFirstById(id);
        return surveyOptional.map(survey -> statisticMapper.toSurveyDTO(survey));
    }
}
