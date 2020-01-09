package com.softserve.academy.event.dto;

import lombok.*;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class QuestionsSeparatelyStatisticDTO {
    private String email;
    private Set<OneQuestionSeparatelyStatisticDTO> questionDTOS;

}
