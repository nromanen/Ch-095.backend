package com.softserve.academy.event.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDTO {

    private String[] emailsArray;
    private String userId;
    private String surveyId;

    public EmailDTO() {
    }

}
