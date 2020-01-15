package com.softserve.academy.event.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.softserve.academy.event.entity.Survey;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EditSurveyQuestionDTO {
    private int index;

    @JsonIgnore
    private Survey survey;

    private String question;

    private String type;

    private List<String> choiceAnswers;

    private List<String> uploadingPhotos = new ArrayList<>();

    private boolean required;

}