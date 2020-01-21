package com.softserve.academy.event.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EditSurveyDTO  {
    private String title;
    private String surveyPhotoName;
    private String surveyType;

    private List<EditSurveyQuestionDTO> questions;
}