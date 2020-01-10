package com.softserve.academy.event.service.db.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.service.db.StatisticService;
import com.softserve.academy.event.service.mapper.GeneralStatisticMapper;
import com.softserve.academy.event.service.mapper.SeparatelyStatisticMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@Slf4j
public class StatisticServiceImpl implements StatisticService {

    private SurveyRepository surveyRepository;
    private GeneralStatisticMapper generalStatisticMapper;
    private SeparatelyStatisticMapper separatelyStatisticMapper;

    @Autowired
    public void setSurveyRepository(SurveyRepository surveyRepository) {
        this.surveyRepository = surveyRepository;
    }

    @Autowired
    public void setGeneralStatisticMapper(GeneralStatisticMapper generalStatisticMapper) {
        this.generalStatisticMapper = generalStatisticMapper;
    }

    @Autowired
    public void setSeparatelyStatisticMapper(SeparatelyStatisticMapper separatelyStatisticMapper) {
        this.separatelyStatisticMapper = separatelyStatisticMapper;
    }

    @Override
    @Transactional
    public QuestionsGeneralStatisticDTO getGeneralStatistic(Long id) {
        log.info("call with id = " + id);
        Optional<Survey> surveyOptional = surveyRepository.findFirstById(id);
        if(surveyOptional.isPresent()){
            try {
                return generalStatisticMapper.toQuestionsDTO(surveyOptional.get());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                return new QuestionsGeneralStatisticDTO();
            }
        }
        else {return new QuestionsGeneralStatisticDTO();}
    }

    @Override
    @Transactional
    public Set<QuestionsSeparatelyStatisticDTO> getSeparatelyStatistic(Long id) {
        log.info("call with id = " + id);
        Optional<Survey> surveyOptional = surveyRepository.findFirstById(id);
        if(surveyOptional.isPresent()){
            try {
                return separatelyStatisticMapper.toSetQuestionsDTO(surveyOptional.get());
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
                return new HashSet<>();
            }
        }
        else {return new HashSet<>();}
    }

    @Override
    public boolean isSurveyBelongsUser(Long surveyId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails =
                    (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Optional<Survey> survey = surveyRepository.findFirstById(surveyId);
            return survey.map(value ->
                    value.getUser().getEmail().equals(userDetails.getUsername()))
                    .orElseThrow(SurveyNotFound::new);
        } else {
            return false;
        }
    }
}
