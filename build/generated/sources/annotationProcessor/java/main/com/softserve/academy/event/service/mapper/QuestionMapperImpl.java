package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-01-08T10:50:33+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.3 (JetBrains s.r.o)"
)
*/
@Component
public class QuestionMapperImpl implements QuestionMapper {

    @Override
    public QuestionDTO toDTO(SurveyQuestion surveyQuestion) {
        if ( surveyQuestion == null ) {
            return null;
        }

        QuestionDTO questionDTO = new QuestionDTO();

        questionDTO.setValue( surveyQuestion.getQuestion() );
        questionDTO.setId( surveyQuestion.getId() );
        questionDTO.setIndex( surveyQuestion.getIndex() );
        questionDTO.setRequired( surveyQuestion.isRequired() );
        questionDTO.setType( surveyQuestion.getType() );
        questionDTO.setChoiceAnswers( surveyQuestion.getChoiceAnswers() );

        return questionDTO;
    }

    @Override
    public List<QuestionDTO> listQuestionToDTO(List<SurveyQuestion> list) {
        if ( list == null ) {
            return null;
        }

        List<QuestionDTO> list1 = new ArrayList<QuestionDTO>( list.size() );
        for ( SurveyQuestion surveyQuestion : list ) {
            list1.add( toDTO( surveyQuestion ) );
        }

        return list1;
    }
}
