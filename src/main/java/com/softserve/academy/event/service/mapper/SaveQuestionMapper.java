package com.softserve.academy.event.service.mapper;


import com.softserve.academy.event.dto.SurveyQuestionDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface SaveQuestionMapper {

//    @Mapping(target = "answers", expression = "java(surveyQuestionDTO.getAnswers().stream().forEach( x -> x = \"\\\"\"+x+\"\\\"\").t).toString()")
    @Mapping(target = "answers", ignore = true)
    SurveyQuestion toEntity(SurveyQuestionDTO surveyQuestionDTO);
}

