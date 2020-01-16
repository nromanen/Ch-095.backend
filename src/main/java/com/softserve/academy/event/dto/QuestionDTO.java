package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuestionDTO {

    private Long id;
    private int index;
    private boolean required;
    private SurveyQuestionType type;
    private String value;
    private String choiceAnswers;
    private List<String> uploadingPhotos = new ArrayList<>();
}
