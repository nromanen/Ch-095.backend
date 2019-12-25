package com.softserve.academy.event.repository;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

import java.util.Optional;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

    Optional<Survey> findFirstByIdAndUserId(Long id, Long userId);

    Optional<Survey> findFirstByIdAndUserIdOrStatus(Long id, Long userId, SurveyStatus status);

    Page<SurveyDTO> findAllByPageableAndUserId(Pageable pageable, Long userId);

    Page<SurveyDTO> findAllByPageableAndStatusUserId(Pageable pageable, String status, Long userId);

    boolean isExistIdAndUserId(Long id, Long userId);

//    public void save(Set<Contact> contactSet);
}
