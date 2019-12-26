package com.softserve.academy.event.repository;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.util.Optional;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

    Page<SurveyDTO> findAllByPageableAndUserEmail(Pageable pageable, String userEmail);

    Page<SurveyDTO> findAllByPageableAndStatusAndUserEmail(Pageable pageable, String status, String userEmail);

    boolean isExistIdAndUserId(Long id, Long userId);

//    public void save(Set<Contact> contactSet);
}
