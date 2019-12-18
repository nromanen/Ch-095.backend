package com.softserve.academy.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerSurveyTitle {
    String title;

    public AnswerSurveyTitle(String title) {
        this.title = title;
    }
}
