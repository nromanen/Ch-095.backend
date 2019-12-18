package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.AnswerDTO;
import com.softserve.academy.event.entity.SurveyAnswer;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2019-12-18T09:30:49+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 1.8.0_201 (Oracle Corporation)"
)
@Component
public class AnswerMapperImpl implements AnswerMapper {

    @Override
    public SurveyAnswer toEntity(AnswerDTO answerDTO) {
        if ( answerDTO == null ) {
            return null;
        }

        SurveyAnswer surveyAnswer = new SurveyAnswer();

        surveyAnswer.setValue( answerDTO.getValue() );

        return surveyAnswer;
    }
}
