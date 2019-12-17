package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.AnswerDTO;
import com.softserve.academy.event.entity.SurveyAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

@Mapper(componentModel = "spring")
@Service
public interface AnswerMapper {

    @Mapping(target = "question", source = "questionId")
    @Mapping(target = "questionContact.contactId", source = "contactId")
    SurveyAnswer toEntity(AnswerDTO answerDTO);
}
