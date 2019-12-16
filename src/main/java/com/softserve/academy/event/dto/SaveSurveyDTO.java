package com.softserve.academy.event.dto;


import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SaveSurveyDTO {

    private String title;

    private long userID;

    private List<SurveyQuestionDTO> questions;

    public List<SurveyQuestion> questionsDtoToEntity(Survey survey) {
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        for (SurveyQuestionDTO surveyQuestionDTO : this.getQuestions()) {
            SurveyQuestion surveyQuestion = new SurveyQuestion();
            surveyQuestion.setAnswers(surveyQuestionDTO.getAnswers().toString());
            surveyQuestion.setIndex(surveyQuestionDTO.getIndex());
            surveyQuestion.setQuestion(surveyQuestionDTO.getQuestion());
            surveyQuestion.setRequired(surveyQuestionDTO.isRequired());
            surveyQuestion.setType(SurveyQuestionType.valueOf(surveyQuestionDTO.getType()));
            surveyQuestion.setSurvey(survey);
            surveyQuestions.add(surveyQuestion);
        }
        return surveyQuestions;
    }

}