package com.softserve.academy.event.dto;


public class SaveSurveyDTO {

    private String title;
    private int userID;

    public SaveSurveyDTO(String title, int userID) {
        this.title = title;
        this.userID = userID;
    }

    public SaveSurveyDTO() {
    }


}
