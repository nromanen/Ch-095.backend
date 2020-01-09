package com.softserve.academy.event.dto;


import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class QuestionsGeneralStatisticDTO {
    String title;
    Set<OneQuestionGeneralStatisticDTO> questionDTOS;
}
