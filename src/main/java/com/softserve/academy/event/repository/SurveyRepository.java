package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;

import java.util.Set;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

//    boolean getEventById(String surveyId);
    public void save(Set<Contact> contactSet);
}
