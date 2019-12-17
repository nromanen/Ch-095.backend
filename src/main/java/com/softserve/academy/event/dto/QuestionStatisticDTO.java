package com.softserve.academy.event.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.entity.SurveyQuestion;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class QuestionStatisticDTO implements Serializable {

    private Long id;
    private String question;
    private String[] answers;
    private int index;
    private static ObjectMapper mapper = new ObjectMapper();

    public static QuestionStatisticDTO toSimpleQuestionDTO(SurveyQuestion surveyQuestion)  {
        try {
            QuestionStatisticDTO dto = new QuestionStatisticDTO();
            dto.answers = mapper.readValue(surveyQuestion.getAnswers(),String[].class);
            dto.id = surveyQuestion.getId();
            dto.index = surveyQuestion.getIndex();
            dto.question = surveyQuestion.getQuestion();
            return dto;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}