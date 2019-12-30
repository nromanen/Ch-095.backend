package com.softserve.academy.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.event.entity.Survey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditSurveyQuestionDTO {
    private int index;

    @JsonIgnore
    private Survey survey;

    private String question;

    private String type;

    private String answers;

    private boolean required;

}