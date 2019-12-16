package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-16T15:41:34+0200",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.4 (JetBrains s.r.o)"
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

        Long id = surveyQuestionSurveyId( surveyQuestion );
        if ( id != null ) {
            questionDTO.setIndex( id.intValue() );
        }
        questionDTO.setValue( surveyQuestion.getQuestion() );
        questionDTO.setRequired( surveyQuestion.isRequired() );
        questionDTO.setType( surveyQuestion.getType() );
        questionDTO.setAnswers( surveyQuestion.getAnswers() );

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

    private Long surveyQuestionSurveyId(SurveyQuestion surveyQuestion) {
        if ( surveyQuestion == null ) {
            return null;
        }
        Survey survey = surveyQuestion.getSurvey();
        if ( survey == null ) {
            return null;
        }
        Long id = survey.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
