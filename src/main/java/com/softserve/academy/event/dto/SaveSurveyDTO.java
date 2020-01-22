package com.softserve.academy.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaveSurveyDTO {

    private String title;
    private String surveyPhotoName;
    private String surveyType;
    private List<SurveyQuestionDTO> questions;
}