package com.softserve.academy.event.dto;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Setter
@Getter
public class QuestionStatisticDTO implements Serializable {

    private int id;
    private String question;
    private String[] answers;
    private int index;


}