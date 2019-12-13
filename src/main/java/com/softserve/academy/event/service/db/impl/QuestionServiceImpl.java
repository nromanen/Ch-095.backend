package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.service.db.QuestionService;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl extends BasicServiceImpl<SurveyQuestion, Long> implements QuestionService {
    @Override
    public SurveyQuestion save(SurveyQuestion entity) {
        return super.save(entity);
    }
}
