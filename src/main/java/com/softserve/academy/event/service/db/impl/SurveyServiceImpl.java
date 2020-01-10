package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.*;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.exception.AccessDeniedException;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.service.mapper.SurveyMapper;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.softserve.academy.event.util.SecurityUserUtil.checkUserEmailNotEqualsCurrentUserEmail;
import static com.softserve.academy.event.util.SecurityUserUtil.getCurrentUserEmail;

@Service
@Transactional
@Slf4j
public class SurveyServiceImpl implements SurveyService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final SurveyRepository repository;
    private final QuestionRepository questionRepository;
    private final SurveyMapper mapper;

    @Autowired
    public SurveyServiceImpl(UserRepository userRepository, SurveyRepository repository, UserService userService, QuestionRepository questionRepository, SurveyMapper mapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.repository = repository;
        this.questionRepository = questionRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SurveyDTO> findAllByPageableAndStatus(Pageable pageable, String status) {
        Page<Survey> surveysPage;
        if (Objects.nonNull(status) && status.length() > 0) {
            surveysPage = repository.findAllByPageableAndStatusAndUserEmail(pageable, SurveyStatus.valueOf(status), getCurrentUserEmail());
        } else {
            surveysPage = repository.findAllByPageableAndUserEmail(pageable, getCurrentUserEmail());
        }
        return new Page<>(
                surveysPage.getItems()
                        .stream()
                        .map(e -> mapper.toDTO(
                                e,
                                e.getSurveyContacts()
                                        .stream()
                                        .filter(SurveyContact::isCanPass)
                                        .count(),
                                (long) e.getSurveyContacts().size()))
                        .collect(Collectors.toList()),
                surveysPage.getPageable()
        );
    }

    @Override
    public void updateTitle(Long id, String title) {
        Survey survey = findSurveyById(id);
        survey.setTitle(title);
        repository.update(survey);
    }

    @Override
    public void updateStatus(Long id, SurveyStatus status) {
        Survey survey = findSurveyById(id);
        survey.setStatus(status);
        repository.update(survey);
    }

    @Override
    public Survey duplicateSurvey(DuplicateSurveySettings settings) {
        return repository.cloneSurvey(settings);
//        Survey survey = repository.eagerFindFirstById(settings.getId());
//        if (!SurveyStatus.TEMPLATE.equals(survey.getStatus()) &&
//                checkUserEmailNotEqualsCurrentUserEmail(survey.getUser().getEmail())) {
//            log.debug("User " + survey.getUser().getUsername() + " try change other user information. ");
//            throw new AccessDeniedException();
//        }
//        repository.detach(survey);
//        survey.setId(null);
//        survey.setStatus(SurveyStatus.NON_ACTIVE);
//        if (settings.isClearContacts()) {
//            survey.setSurveyContacts(new HashSet<>());
//        } else {
//            survey.getSurveyContacts().forEach(e -> {
//                e.setId(null);
//                e.setSurvey(survey);
//                e.setCanPass(false);
//            });
//        }
//        survey.getSurveyQuestions()
//                .forEach(this::clearSurveyAnswers);
//        repository.save(survey);
//        return survey;
    }

    private void clearSurveyAnswers(SurveyQuestion surveyQuestion) {
        surveyQuestion.setSurveyAnswers(new HashSet<>());
    }

    private Survey findSurveyById(Long id) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(SurveyNotFound::new);
        if (checkUserEmailNotEqualsCurrentUserEmail(survey.getUser().getEmail())) {
            log.debug("User " + survey.getUser().getUsername() + " try change other user information. ");
            throw new AccessDeniedException();
        }
        return survey;
    }

    @Override
    public void delete(Long id) {
        Survey survey = findSurveyById(id);
        if (survey.isActive()) {
            survey.setActive(false);
            repository.update(survey);
        } else {
            repository.delete(survey);
        }
    }

    @Override
    public Optional<Survey> findFirstById(long surveyId) {
        return repository.findFirstById(surveyId);
    }

    @Override
    public Survey saveSurveyWithQuestions(Survey survey, List<SurveyQuestion> surveyQuestions) {
        Long userID = userService.getAuthenticationId()
                .orElseThrow(RuntimeException::new);
        User user = userRepository.findFirstById(userID)
                .orElseThrow(UserNotFound::new);
        survey.setUser(user);
        surveyQuestions.forEach(survey::addQuestion);
        return repository.save(survey);
    }

    public Survey editSurvey(Long surveyId, List<SurveyQuestion> surveyQuestions) {
        Survey survey = repository.findFirstById(surveyId)
                .orElseThrow(SurveyNotFound::new);
        survey.getSurveyQuestions().forEach(questionRepository::delete);
        survey.getSurveyQuestions().clear();
        surveyQuestions.forEach(survey::addQuestion);
        return repository.update(survey);
    }

}
