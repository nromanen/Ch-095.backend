package com.softserve.academy.event.repository;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.util.Optional;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

    Page<SurveyDTO> findAllByPageable(Pageable pageable, User user);

    Page<SurveyDTO> findAllByPageableAndStatus(Pageable pageable, String status, User user);

//    public void save(Set<Contact> contactSet);
}
