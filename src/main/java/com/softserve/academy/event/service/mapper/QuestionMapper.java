package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper(componentModel = "spring")
@Service
public interface QuestionMapper {

    @Mapping(target = "index", source = "survey.id")
    @Mapping(target = "value", source = "question")
    QuestionDTO toDTO(SurveyQuestion surveyQuestion);

    List<QuestionDTO> listQuestionToDTO(List<SurveyQuestion> list);

}
