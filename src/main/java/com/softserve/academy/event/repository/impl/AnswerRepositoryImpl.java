package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.repository.AnswerRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerRepositoryImpl extends BasicRepositoryImpl<SurveyAnswer, Long> implements AnswerRepository {
}
