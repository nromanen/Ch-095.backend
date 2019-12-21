package com.softserve.academy.event.service.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.SurveyQuestionDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface SaveQuestionMapper {

//    @Mapping(target = "answers", expression = "java(new ObjectMapper().writeValueAsString(surveyQuestionDTO.getAnswers()))")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answers", ignore = true)
    SurveyQuestion toEntity(SurveyQuestionDTO surveyQuestionDTO) throws JsonProcessingException;
}

