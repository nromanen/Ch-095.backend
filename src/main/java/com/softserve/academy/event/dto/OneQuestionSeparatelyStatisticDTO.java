package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OneQuestionSeparatelyStatisticDTO {
    private String question;
    private SurveyQuestionType type;
    private String[] choiceAnswers;
    private int index;
    private List<String> answer;


}
