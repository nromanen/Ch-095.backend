package com.softserve.academy.event.repository.impl;

import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.repository.AnswerRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AnswerRepositoryImpl extends BasicRepositoryImpl<SurveyAnswer, Long> implements AnswerRepository {

    @Override
    public List<SurveyAnswer> findByQuestionId(Long questionId) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("SurveyAnswer.findByQuestionId")
                .setParameter("questionId",questionId).getResultList();
    }

    @Override
    public List<SurveyAnswer> saveAll(List<SurveyAnswer> answers) {
        Session session = sessionFactory.getCurrentSession();
        answers.forEach(session::save);
        return answers;
    }
}