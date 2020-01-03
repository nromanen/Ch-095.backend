package com.softserve.academy.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class EditSurveyDTO  {

    private String title;
    private String surveyPhotoName;
    private List<EditSurveyQuestionDTO> questions;

    public EditSurveyDTO(List<EditSurveyQuestionDTO> questions){
        this.questions = questions;
    }
}