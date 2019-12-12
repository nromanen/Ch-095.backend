package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionDTO {

    private int index;
    private boolean required;
    private SurveyQuestionType type;
    private String value;
    private String answers;
}
