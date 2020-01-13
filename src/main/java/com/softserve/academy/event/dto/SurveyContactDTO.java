package com.softserve.academy.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SurveyContactDTO {

    private Long surveyId;
    private String contactEmail;
    private List<QuestionDTO> questions;
}