package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.*;

@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final SurveyRepository repository;
    private UserRepository userRepository;
    private QuestionRepository questionRepository;

    @Autowired
    public SurveyServiceImpl(SurveyRepository repository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Page<Survey> findAllByPageable(Pageable pageable) {
        return repository.findAllByPageable(pageable);
    }


    @Override
    public Page<Survey> findAllByPageableAndStatus(Pageable pageable, String status) {
        return repository.findAllByPageableAndStatus(pageable, status);
    }

//    @Override
//    public Page<Survey> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters) {
//        return repository.findAllFiltered(pageable,
//                Objects.nonNull(filters) ? filters :
//                        Collections.singletonMap("surveyStatusField",
//                                Collections.singletonMap("status", SurveyStatus.TEMPLATE.getNumber()))
//        );
//    }

    @Override
    public void updateTitle(Long id, String title) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        repository.update(survey);
    }

    @Override
    public void updateStatus(Long id, SurveyStatus status) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(RuntimeException::new);
        survey.setStatus(status);
        repository.update(survey);
    }

    @Override
    public Survey duplicateSurvey(DuplicateSurveySettings settings) {
        Survey survey = repository.findFirstById(settings.getId())
                .orElseThrow(RuntimeException::new);
        repository.detach(survey);
        survey.setId(null);
        survey.setStatus(SurveyStatus.NON_ACTIVE);
        if (settings.isClearContacts()) {
            survey.setContacts(new HashSet<>());
        }
        repository.save(survey);
        return survey;
    }

    @Override
    public void delete(Survey entity) {
        repository.delete(entity);
    }

    @Override
    public Optional<Survey> findFirstById(long surveyId) {
        return repository.findFirstById(surveyId);
    }

    @Override
    public Survey saveSurveyWithQuestions(Survey survey, long id, List<SurveyQuestion> surveyQuestions) {
        User user = userRepository.findFirstById(id).get();
        survey.setUser(user);
        Survey savedSurvey = repository.save(survey);
        surveyQuestions.forEach((x) -> x.setSurvey(savedSurvey));
        surveyQuestions.forEach(questionRepository::save);
        return savedSurvey;

    }
}
