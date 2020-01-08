package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import org.springframework.stereotype.Component;

/*
@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2020-01-08T10:50:33+0300",
    comments = "version: 1.3.1.Final, compiler: javac, environment: Java 11.0.3 (JetBrains s.r.o)"
)
*/
@Component
public class SurveyMapperImpl implements SurveyMapper {

    @Override
    public SurveyDTO toDTO(Survey survey) {
        if ( survey == null ) {
            return null;
        }

        SurveyDTO surveyDTO = new SurveyDTO();

        surveyDTO.setId( survey.getId() );
        surveyDTO.setTitle( survey.getTitle() );
        surveyDTO.setStatus( survey.getStatus() );
        surveyDTO.setImageUrl( survey.getImageUrl() );

        return surveyDTO;
    }
}
