package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Data
public class OneQuestionSeparatelyStatisticDTO {
    private String question;
    private SurveyQuestionType type;
    private String[] choiceAnswers;
    private int index;
    private List<String> answer;


}
