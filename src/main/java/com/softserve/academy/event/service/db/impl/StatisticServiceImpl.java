package com.softserve.academy.event.service.db.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.SurveyEachStatisticDTO;
import com.softserve.academy.event.dto.SurveyGeneralStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.service.db.StatisticService;
import com.softserve.academy.event.service.mapper.StatisticMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


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
    public Optional<SurveyGeneralStatisticDTO> getGeneralStatistic(Long id) {
        log.info("call with id = " + id);
        Optional<Survey> surveyOptional = surveyRepository.findFirstById(id);
        return surveyOptional.map(survey -> statisticMapper.toSurveyGeneralDTO(survey));
    }

    @Transactional
    public Optional<SurveyEachStatisticDTO> getEachStatistic(Long id) {
        log.info("call with id = " + id);
        Optional<Survey> surveyOptional = surveyRepository.findFirstById(id);
        return surveyOptional.map(survey -> statisticMapper.toSurveyEachDTO(survey));
    }
}
