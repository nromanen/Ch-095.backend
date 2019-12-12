package com.softserve.academy.event.dto;


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

}
