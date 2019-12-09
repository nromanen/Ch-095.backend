package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.repository.QuestionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuestionRepositoryImpl extends BasicRepositoryImpl<SurveyQuestion, Long> implements QuestionRepository{
}
