package com.softserve.academy.event.service.db.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.QuestionStatisticDTO;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.exception.QuestionNotFoundException;
import com.softserve.academy.event.repository.AnswerRepository;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.service.db.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.util.*;

@Service
@Transactional
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<SurveyAnswer> findFirstById(Long id) {
        return answerRepository.findFirstById(id);
    }

    @Override
    public List<SurveyAnswer> findAll() {
        return answerRepository.findAll();
    }

    @Override
    public SurveyAnswer save(SurveyAnswer entity) {
        return answerRepository.save(entity);
    }

    @Override
    public SurveyAnswer update(SurveyAnswer object) {
        return answerRepository.update(object);
    }

    @Override
    public void delete(SurveyAnswer entity) {
        answerRepository.delete(entity);
    }

    @Override
    public void detach(SurveyAnswer entity) {
        answerRepository.detach(entity);
    }

    @Override
    public Map<String, Integer> createStatisticAnswersMap(Long questionId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<SurveyAnswer> answers = answerRepository.findByQuestionId(questionId);
        List<String> answersType = Arrays.asList(mapper.readValue(
                questionRepository.findFirstById(questionId).orElseThrow(
                        () -> new QuestionNotFoundException("No question with " + questionId))
                        .getAnswers(),String[].class));

        Map<String,Integer> statisticMap = new HashMap<>();
        answersType.forEach(answer -> {
            statisticMap.put(answer,0);
        });
        answers.forEach(surveyAnswer -> {
            statisticMap.put(surveyAnswer.getValue()
                    ,1 + statisticMap.get(surveyAnswer.getValue()));
        });

        return statisticMap;
    }
}
