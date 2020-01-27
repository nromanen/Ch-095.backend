package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyType;
import com.softserve.academy.event.exception.SurveyNotBelongUser;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.service.db.StatisticService;
import com.softserve.academy.event.service.mapper.GeneralStatisticMapper;
import com.softserve.academy.event.service.mapper.SeparatelyStatisticMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.softserve.academy.event.util.SecurityUserUtil.getCurrentUserEmail;


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
        return surveyOptional.map(survey -> generalStatisticMapper.toQuestionsDTO(survey))
                .orElseThrow(SurveyNotFound::new);
    }

    @Override
    @Transactional
    public Set<QuestionsSeparatelyStatisticDTO> getSeparatelyStatistic(Long id) {
        log.info("call with id = " + id);
        Optional<Survey> surveyOptional = surveyRepository.findFirstById(id);
        return surveyOptional.map(survey -> {
            if(survey.getType() == SurveyType.INDIVIDUAL) {
                return separatelyStatisticMapper.toSetQuestionsDTO(survey);
            }
            return new HashSet<QuestionsSeparatelyStatisticDTO>();
        })
                .orElseThrow(SurveyNotFound::new);
    }

    @Override
    @Transactional
    public void isSurveyBelongsUser(Long surveyId) {
        Optional<Survey> survey = surveyRepository.findFirstById(surveyId);

        if (!survey.isPresent()) {
            log.info("Throw SurveyNotFound");
            throw new SurveyNotFound();
        }

        if (!survey.get().getUser().getEmail().equals(getCurrentUserEmail())) {
            log.info("Throw SurveyNotBelongUser");
            throw new SurveyNotBelongUser();
        }


    }

}
