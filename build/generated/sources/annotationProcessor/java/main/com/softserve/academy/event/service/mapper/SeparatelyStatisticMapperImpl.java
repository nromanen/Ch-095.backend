package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.OneQuestionSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-01-08T10:50:33+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.3 (JetBrains s.r.o)"
)
*/
@Component
public class SeparatelyStatisticMapperImpl implements SeparatelyStatisticMapper {

    @Override
    public QuestionsSeparatelyStatisticDTO toQuestionsDTO(Contact contact, Survey survey) {
        if ( contact == null && survey == null ) {
            return null;
        }

        QuestionsSeparatelyStatisticDTO questionsSeparatelyStatisticDTO = new QuestionsSeparatelyStatisticDTO();

        questionsSeparatelyStatisticDTO.setQuestionDTOS( toSetQuestionDTO(survey.getSurveyQuestions(),contact) );
        questionsSeparatelyStatisticDTO.setEmail( contact.getEmail() );

        return questionsSeparatelyStatisticDTO;
    }

    @Override
    public OneQuestionSeparatelyStatisticDTO toQuestionDTO(SurveyQuestion surveyQuestion, Contact contact) throws JsonProcessingException {
        if ( surveyQuestion == null && contact == null ) {
            return null;
        }

        OneQuestionSeparatelyStatisticDTO oneQuestionSeparatelyStatisticDTO = new OneQuestionSeparatelyStatisticDTO();

        if ( surveyQuestion != null ) {
            oneQuestionSeparatelyStatisticDTO.setQuestion( surveyQuestion.getQuestion() );
            oneQuestionSeparatelyStatisticDTO.setType( surveyQuestion.getType() );
            oneQuestionSeparatelyStatisticDTO.setIndex( surveyQuestion.getIndex() );
        }
        oneQuestionSeparatelyStatisticDTO.setAnswer( transformationToAnswer(surveyQuestion,contact) );
        oneQuestionSeparatelyStatisticDTO.setChoiceAnswers( new ObjectMapper().readValue(surveyQuestion.getChoiceAnswers(),String[].class) );

        return oneQuestionSeparatelyStatisticDTO;
    }
}
