package com.softserve.academy.event.dto;


import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;

import java.util.ArrayList;
import java.util.List;

public class SaveSurveyDTO {

    private String title;

    private long userID;

    private List<SurveyQuestionDTO> questions;

    public SaveSurveyDTO() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public List<SurveyQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SurveyQuestionDTO> questions) {
        this.questions = questions;
    }

    public List<SurveyQuestion> questionsDtoToEntity(Survey survey) {
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        for (SurveyQuestionDTO surveyQuestionDTO : this.getQuestions()) {
            SurveyQuestion surveyQuestion = new SurveyQuestion();
            surveyQuestion.setAnswers(surveyQuestionDTO.getAnswers().toString());
            surveyQuestion.setIndex(surveyQuestionDTO.getIndex());
            surveyQuestion.setQuestion(surveyQuestion.getQuestion());
            surveyQuestion.setRequired(surveyQuestionDTO.isRequired());
            surveyQuestion.setType(SurveyQuestionType.valueOf(surveyQuestionDTO.getType()));
            surveyQuestion.setSurvey(survey);
        }
        return surveyQuestions;
    }

}