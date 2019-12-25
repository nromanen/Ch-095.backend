package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.exception.SurveyNotFound;
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
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public SurveyServiceImpl(SurveyRepository repository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Page<SurveyDTO> findAllByPageableAndStatus(Pageable pageable, String status, User user) {
        if (Objects.nonNull(status) && status.length() > 0) {
            return repository.findAllByPageableAndStatusUserId(pageable, status, user.getId());
        }
        return repository.findAllByPageableAndUserId(pageable, user.getId());
    }

    @Override
    public void updateTitle(Long id, String title) {
        Survey survey = repository.findFirstByIdAndUserId(id, 1L) // todo change to getId
                .orElseThrow(SurveyNotFound::new);
        survey.setTitle(title);
        repository.update(survey);
    }

    @Override
    public void updateStatus(Long id, SurveyStatus status) {
        Survey survey = repository.findFirstByIdAndUserId(id, 1L) // todo change to getId
                .orElseThrow(SurveyNotFound::new);
        survey.setStatus(status);
        repository.update(survey);
    }

    @Override
    public Survey duplicateSurvey(DuplicateSurveySettings settings) {
        Survey survey = repository.findFirstByIdAndUserIdOrStatus(settings.getId(), 1L, SurveyStatus.TEMPLATE) // todo change to getId
                .orElseThrow(SurveyNotFound::new);
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
        if (repository.isExistIdAndUserId(entity.getId(), 1L)) {
            repository.delete(entity);
        } else {
            throw new SurveyNotFound();
        }
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
