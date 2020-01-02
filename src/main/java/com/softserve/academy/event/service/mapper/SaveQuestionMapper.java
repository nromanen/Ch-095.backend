package com.softserve.academy.event.service.mapper;


import com.softserve.academy.event.dto.EditSurveyQuestionDTO;
import com.softserve.academy.event.dto.SurveyQuestionDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper(componentModel = "spring")
@Service
public interface SaveQuestionMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "choiceAnswers", ignore = true)
    SurveyQuestion toEntity(SurveyQuestionDTO surveyQuestionDTO);

    List<EditSurveyQuestionDTO> toDTO(List<SurveyQuestion> surveyQuestions);
}

