package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.util.Map;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

    Page<Survey> findAll(Pageable pageable);

    Page<Survey> findAllByPageableAndStatus(Pageable pageable, String status);

//    public void save(Set<Contact> contactSet);
}
