package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.exception.UnauthorizedException;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
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
    public Page<SurveyDTO> findAllByPageableAndStatus(Pageable pageable, String status) {
        if (Objects.nonNull(status) && status.length() > 0) {
            return repository.findAllByPageableAndStatusAndUserEmail(pageable, status, getCurrentUserDetails().getUsername());
        }
        return repository.findAllByPageableAndUserEmail(pageable, getCurrentUserDetails().getUsername());
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
        Survey survey = repository.findFirstById(settings.getId())
                .orElseThrow(SurveyNotFound::new);
        if (!survey.getStatus().equals(SurveyStatus.TEMPLATE) &&
                checkUserEmailNotEqualsCurrentUserEmail(survey.getUser().getEmail())) {
            log.debug("User " + survey.getUser().getUsername() + " try change other user information. ");
            throw new SurveyNotFound();
        }
        repository.detach(survey);
        survey.setId(null);
        survey.setStatus(SurveyStatus.NON_ACTIVE);
        if (settings.isClearContacts()) {
            survey.setContacts(new HashSet<>());
        }
        repository.save(survey);
        return survey;
    }

    private Survey findSurveyById(Long id) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(SurveyNotFound::new);
        if (checkUserEmailNotEqualsCurrentUserEmail(survey.getUser().getEmail())) {
            log.debug("User " + survey.getUser().getUsername() + " try change other user information. ");
            throw new SurveyNotFound();
        }
        return survey;
    }

    private boolean checkUserEmailNotEqualsCurrentUserEmail(String email) {
        return !email.equals(getCurrentUserDetails().getUsername());
    }

    private UserDetails getCurrentUserDetails() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails instanceof UserDetails) {
            return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } else {
            throw new UnauthorizedException();
        }
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
    public Survey saveSurveyWithQuestions(Survey survey, long id, List<SurveyQuestion> surveyQuestions) {
        User user = userRepository.findFirstById(id).get();
        survey.setUser(user);
        Survey savedSurvey = repository.save(survey);
        surveyQuestions.forEach((x) -> x.setSurvey(savedSurvey));
        surveyQuestions.forEach(questionRepository::save);
        return savedSurvey;

    }
}
