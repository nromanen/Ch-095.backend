package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.SurveyQuestionDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import org.springframework.stereotype.Component;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-16T15:41:34+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.4 (JetBrains s.r.o)"
)
*/
@Component
public class SaveQuestionMapperImpl implements SaveQuestionMapper {

    @Override
    public SurveyQuestion toEntity(SurveyQuestionDTO surveyQuestionDTO) {
        if ( surveyQuestionDTO == null ) {
            return null;
        }

        SurveyQuestion surveyQuestion = new SurveyQuestion();

        surveyQuestion.setIndex( surveyQuestionDTO.getIndex() );
        surveyQuestion.setSurvey( surveyQuestionDTO.getSurvey() );
        surveyQuestion.setQuestion( surveyQuestionDTO.getQuestion() );
        if ( surveyQuestionDTO.getType() != null ) {
            surveyQuestion.setType( Enum.valueOf( SurveyQuestionType.class, surveyQuestionDTO.getType() ) );
        }
        surveyQuestion.setRequired( surveyQuestionDTO.isRequired() );

        return surveyQuestion;
    }
}
