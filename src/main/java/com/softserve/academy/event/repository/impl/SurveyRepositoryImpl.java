package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;

@Repository
public class SurveyRepositoryImpl extends PaginationRepositoryImpl<Survey, Long> {

    public Page<Survey> findAll(Pageable pageable) {
        return super.findAll(pageable);
    }

}
