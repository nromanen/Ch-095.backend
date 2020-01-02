package com.softserve.academy.event.service.mapper;


import com.softserve.academy.event.dto.SurveyQuestionDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface SaveQuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "answers", ignore = true)
    SurveyQuestion toEntity(SurveyQuestionDTO surveyQuestionDTO);

}

