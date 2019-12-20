package com.softserve.academy.event.repository;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.security.Principal;
import java.util.Map;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

    Page<Survey> findAllByPageable(Pageable pageable, User user);

    Page<Survey> findAllByPageableAndStatus(Pageable pageable, String status, User user);

//    public void save(Set<Contact> contactSet);
}
