package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.repository.SurveyReporitory;
import org.springframework.stereotype.Repository;

@Repository
public class SurveyRepositoryImpl extends BasicRepositoryImpl<Survey, Long> implements SurveyReporitory {
}
