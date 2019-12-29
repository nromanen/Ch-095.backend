package com.softserve.academy.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class SurveyGeneralStatisticDTO {
    String title;
    Set<QuestionStatisticDTO> questionDTOS;
}
