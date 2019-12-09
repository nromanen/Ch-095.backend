package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SimpleSurveyDTO {

    private Long id;
    private String title;
    private Date creationDate;
    private SurveyStatus status;

    private SimpleSurveyDTO() {
    }

    public static SimpleSurveyDTO toSimpleUser(Survey survey) {
        SimpleSurveyDTO dto = new SimpleSurveyDTO();
        dto.setId(survey.getId());
        dto.setTitle(survey.getTitle());
        dto.setStatus(survey.getStatus());
        dto.setCreationDate(survey.getCreationDate());
        return dto;
    }

}
