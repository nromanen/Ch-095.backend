package com.softserve.academy.event.dto;

import com.softserve.academy.event.entity.enums.SurveyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class SurveyDTO {

    private Long id;
    private String title;
    private SurveyStatus status;
    private String imageUrl;

}
