package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.QuestionStatisticDTO;
import com.softserve.academy.event.dto.SurveyStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",imports = {ObjectMapper.class, Collectors.class, Collection.class})
@Service
public interface StatisticMapper {

    @Mapping(target = "questionDTOS", source = "surveyQuestions")
    SurveyStatisticDTO toSurveyDTO(Survey survey);

    @Mapping(target = "choiceAnswers", expression = "java(new ObjectMapper().readValue(" +
            "surveyQuestion.getChoiceAnswers(),String[].class))")
    @Mapping(target = "answers", expression = "java(surveyQuestion.getSurveyAnswers()" +
            ".stream().map(surveyAnswer -> surveyAnswer.getValue()).collect(Collectors.toList()))")
    QuestionStatisticDTO toQuestionDTO(SurveyQuestion surveyQuestion) throws JsonProcessingException;

    Set<QuestionStatisticDTO> listQuestionStatisticToDTO(Set<SurveyQuestion> surveyQuestions);
}
