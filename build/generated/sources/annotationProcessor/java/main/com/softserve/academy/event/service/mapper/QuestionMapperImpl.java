package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.QuestionStatisticDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-18T13:45:45+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_201 (Oracle Corporation)"
)
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

    @Override
    public QuestionStatisticDTO toStatisticDTO(SurveyQuestion surveyQuestion) throws JsonProcessingException {
        if ( surveyQuestion == null ) {
            return null;
        }

        QuestionStatisticDTO questionStatisticDTO = new QuestionStatisticDTO();

        if ( surveyQuestion.getId() != null ) {
            questionStatisticDTO.setId( surveyQuestion.getId().intValue() );
        }
        questionStatisticDTO.setQuestion( surveyQuestion.getQuestion() );
        questionStatisticDTO.setIndex( surveyQuestion.getIndex() );

        questionStatisticDTO.setAnswers( new ObjectMapper().readValue(surveyQuestion.getAnswers(),String[].class) );

        return questionStatisticDTO;
    }

    @Override
    public List<QuestionStatisticDTO> listQuestionToStatisticDTO(List<SurveyQuestion> list) {
        if ( list == null ) {
            return null;
        }

        List<QuestionStatisticDTO> list1 = new ArrayList<QuestionStatisticDTO>( list.size() );
        for ( SurveyQuestion surveyQuestion : list ) {
            try {
                list1.add( toStatisticDTO( surveyQuestion ) );
            }
            catch ( JsonProcessingException e ) {
                throw new RuntimeException( e );
            }
        }

        return list1;
    }
}
