package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.OneQuestionGeneralStatisticDTO;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-01-08T10:50:32+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.3 (JetBrains s.r.o)"
)
*/
@Component
public class GeneralStatisticMapperImpl implements GeneralStatisticMapper {

    @Override
    public QuestionsGeneralStatisticDTO toQuestionsDTO(Survey survey) {
        if ( survey == null ) {
            return null;
        }

        QuestionsGeneralStatisticDTO questionsGeneralStatisticDTO = new QuestionsGeneralStatisticDTO();

        try {
            questionsGeneralStatisticDTO.setQuestionDTOS( surveyQuestionListToOneQuestionGeneralStatisticDTOSet( survey.getSurveyQuestions() ) );
        }
        catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        }
        questionsGeneralStatisticDTO.setTitle( survey.getTitle() );

        return questionsGeneralStatisticDTO;
    }

    @Override
    public OneQuestionGeneralStatisticDTO toQuestionDTO(SurveyQuestion surveyQuestion) throws JsonProcessingException {
        if ( surveyQuestion == null ) {
            return null;
        }

        OneQuestionGeneralStatisticDTO oneQuestionGeneralStatisticDTO = new OneQuestionGeneralStatisticDTO();

        oneQuestionGeneralStatisticDTO.setQuestion( surveyQuestion.getQuestion() );
        oneQuestionGeneralStatisticDTO.setType( surveyQuestion.getType() );
        oneQuestionGeneralStatisticDTO.setIndex( surveyQuestion.getIndex() );

        oneQuestionGeneralStatisticDTO.setAnswers( transformationToAnswers(surveyQuestion) );
        oneQuestionGeneralStatisticDTO.setChoiceAnswers( new ObjectMapper().readValue(surveyQuestion.getChoiceAnswers(),String[].class) );

        return oneQuestionGeneralStatisticDTO;
    }

    @Override
    public List<OneQuestionGeneralStatisticDTO> listQuestionToDTO(List<SurveyQuestion> surveyQuestions) {
        if ( surveyQuestions == null ) {
            return null;
        }

        List<OneQuestionGeneralStatisticDTO> list = new ArrayList<OneQuestionGeneralStatisticDTO>( surveyQuestions.size() );
        for ( SurveyQuestion surveyQuestion : surveyQuestions ) {
            try {
                list.add( toQuestionDTO( surveyQuestion ) );
            }
            catch ( JsonProcessingException e ) {
                throw new RuntimeException( e );
            }
        }

        return list;
    }

    protected Set<OneQuestionGeneralStatisticDTO> surveyQuestionListToOneQuestionGeneralStatisticDTOSet(List<SurveyQuestion> list) throws JsonProcessingException {
        if ( list == null ) {
            return null;
        }

        Set<OneQuestionGeneralStatisticDTO> set = new HashSet<OneQuestionGeneralStatisticDTO>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( SurveyQuestion surveyQuestion : list ) {
            set.add( toQuestionDTO( surveyQuestion ) );
        }

        return set;
    }
}
