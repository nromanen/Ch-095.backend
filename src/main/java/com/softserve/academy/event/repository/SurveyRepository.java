package com.softserve.academy.event.repository;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;

public interface SurveyRepository extends BasicRepository<Survey, Long> {

    Survey eagerFindFirstById(Long id);

    Page<SurveyDTO> findAllByPageableAndUserEmail(Pageable pageable, String userEmail);

    Page<SurveyDTO> findAllByPageableAndStatusAndUserEmail(Pageable pageable, String status, String userEmail);

}
