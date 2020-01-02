package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.*;
import com.softserve.academy.event.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {ObjectMapper.class, Collectors.class,
        Arrays.class})
@Service
public interface GeneralStatisticMapper {

    @Mapping(target = "questionDTOS", source = "surveyQuestions")
    QuestionsGeneralStatisticDTO toQuestionsDTO(Survey survey);

    @Mapping(target = "choiceAnswers", expression = "java(new ObjectMapper().readValue(" +
            "surveyQuestion.getChoiceAnswers(),String[].class))")
    @Mapping(target = "answers", expression = "java(transformationToAnswers(surveyQuestion))")
    OneQuestionGeneralStatisticDTO toQuestionDTO(SurveyQuestion surveyQuestion) throws JsonProcessingException;

    List<OneQuestionGeneralStatisticDTO> listQuestionToDTO(List<SurveyQuestion> surveyQuestions);

    default List<List<String>> transformationToAnswers(SurveyQuestion surveyQuestion) {
        return surveyQuestion.getSurveyAnswers().stream().map(surveyAnswer -> {
            try {
                return Arrays.asList(new ObjectMapper().readValue(surveyAnswer.getValue(), String[].class));
            } catch (JsonProcessingException e) {
                return null;
            }
        }).collect(Collectors.toList());
    }
}
