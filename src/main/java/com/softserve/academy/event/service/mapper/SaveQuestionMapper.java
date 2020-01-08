package com.softserve.academy.event.service.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.EditSurveyQuestionDTO;
import com.softserve.academy.event.dto.SurveyQuestionDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Mapper(componentModel = "spring", imports = {Arrays.class, ObjectMapper.class, String.class})
@Service
public interface SaveQuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "choiceAnswers",
            expression = "java(new ObjectMapper().writeValueAsString(surveyQuestionDTO.getChoiceAnswers()))")
    SurveyQuestion toEntity(SurveyQuestionDTO surveyQuestionDTO) throws JsonProcessingException;

    @Mapping(target = "choiceAnswers", expression = "java(Arrays.asList(new ObjectMapper().readValue(surveyQuestions.getChoiceAnswers(),String[].class)))")
    SurveyQuestionDTO toDTO(SurveyQuestion surveyQuestions) throws JsonProcessingException;

    @Mapping(target = "files", ignore = true)
    @Mapping(target = "choiceAnswers", expression = "java(Arrays.asList(new ObjectMapper().readValue(surveyQuestions.getChoiceAnswers(),String[].class)))")
    EditSurveyQuestionDTO toEditDTO(SurveyQuestion surveyQuestions) throws JsonProcessingException;
}

