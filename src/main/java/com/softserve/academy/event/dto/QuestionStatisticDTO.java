package com.softserve.academy.event.dto;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.entity.SurveyQuestion;

import java.io.Serializable;

public class QuestionStatisticDTO implements Serializable {

    private Long id;
    private String question;
    private String[] answers;
    private static ObjectMapper mapper = new ObjectMapper();

    public static QuestionStatisticDTO toSimpleQuestionDTO(SurveyQuestion surveyQuestion)  {
        try {
            QuestionStatisticDTO dto = new QuestionStatisticDTO();
            dto.answers = mapper.readValue(surveyQuestion.getAnswers(),String[].class);
            dto.id = surveyQuestion.getId();
            dto.question = surveyQuestion.getQuestion();
            return dto;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }
}