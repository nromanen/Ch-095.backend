package com.softserve.academy.event.util;

import com.softserve.academy.event.entity.enums.SurveyStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SurveyPage<T> extends Page<T> {

    private SurveyStatus[] statuses = SurveyStatus.values();

}
