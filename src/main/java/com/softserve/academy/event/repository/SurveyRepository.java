package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

    Page<Survey> findAll(Pageable pageable);

    HttpStatus updateTitle(Long id, String title);

    Survey duplicateSurvey(Long id);

    String setTitleForSurvey(Long id, String title);

//    public void save(Set<Contact> contactSet);
}
