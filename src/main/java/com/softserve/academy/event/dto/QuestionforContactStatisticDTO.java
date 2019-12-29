package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionforContactStatisticDTO {
    private String question;
    private SurveyQuestionType type;
    private String[] choiceAnswers;
    private int index;
    private List<String> answers;
}
