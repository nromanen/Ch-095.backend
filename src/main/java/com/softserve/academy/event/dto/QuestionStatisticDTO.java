package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class QuestionStatisticDTO implements Serializable {

    private String question;
    private SurveyQuestionType type;
    private String[] choiceAnswers;
    private int index;
    private List<String> answers;

}