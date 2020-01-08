package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.AnswerDTO;
import com.softserve.academy.event.entity.SurveyAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-01-08T10:50:33+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.3 (JetBrains s.r.o)"
)
*/
@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Autowired
    private AnswerMapperResolver answerMapperResolver;

    @Override
    public SurveyAnswer toEntity(AnswerDTO answerDTO) {
        if ( answerDTO == null ) {
            return null;
        }

        SurveyAnswer surveyAnswer = new SurveyAnswer();

        surveyAnswer.setQuestion( answerMapperResolver.getQuestion( answerDTO.getQuestionId() ) );
        try {
            surveyAnswer.setValue( answerMapperResolver.getAnswers( answerDTO.getAnswers() ) );
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        surveyAnswer.setContact( answerMapperResolver.getContact( answerDTO.getContactId() ) );

        return surveyAnswer;
    }
}
