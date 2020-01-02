package com.softserve.academy.event.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class QuestionsGeneralStatisticDTO {
    String title;
    Set<OneQuestionGeneralStatisticDTO> questionDTOS;
}