package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.exception.UnauthorizedException;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final UserRepository userRepository;
    private final UserService userService;
    private final SurveyRepository repository;
    private final QuestionRepository questionRepository;

    @Autowired
    public SurveyServiceImpl(UserRepository userRepository, SurveyRepository repository, UserService userService, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.repository = repository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Page<SurveyDTO> findAllByPageableAndStatus(Pageable pageable, String status) {
        if (Objects.nonNull(status) && status.length() > 0) {
            return repository.findAllByPageableAndStatusAndUserEmail(pageable, status, getCurrentUserEmail());
        }
        return repository.findAllByPageableAndUserEmail(pageable, getCurrentUserEmail());
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
        Survey survey = repository.eagerFindFirstById(settings.getId());
        if (!survey.getStatus().equals(SurveyStatus.TEMPLATE) &&
                checkUserEmailNotEqualsCurrentUserEmail(survey.getUser().getEmail())) {
            log.debug("User " + survey.getUser().getUsername() + " try change other user information. ");
            throw new SurveyNotFound();
        }
        repository.detach(survey);
        survey.setId(null);
        survey.setStatus(SurveyStatus.NON_ACTIVE);
        if (settings.isClearContacts()) {
            survey.setSurveyContacts(new HashSet<>());
        } else {
            survey.getSurveyContacts().forEach(e -> {
                e.setId(null);
                e.setSurvey(survey);
                e.setCanPass(false);
            });
        }
        survey.getSurveyQuestions().forEach(e -> e.setSurveyAnswers(new HashSet<>()));
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
        return !email.equals(getCurrentUserEmail());
    }

    private String getCurrentUserEmail() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        } else if (userDetails instanceof DefaultOidcUser) {
            return ((DefaultOidcUser)userDetails).getEmail();                     // for google
        } else if (userDetails instanceof DefaultOAuth2User) {
            return ((DefaultOAuth2User)userDetails).getAttribute("email"); // for facebook
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
    public Survey saveSurveyWithQuestions(Survey survey, List<SurveyQuestion> surveyQuestions) {
        String email = userService.getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFound::new);
        survey.setUser(user);
        surveyQuestions.forEach(survey::addQuestion);
        return repository.save(survey);
    }

    @Override
    public Survey updateSurvey(Long surveyId, List<SurveyQuestion> surveyQuestions) {
        Survey survey = repository.findFirstById(surveyId).orElseThrow(SurveyNotFound::new);
        survey.getSurveyQuestions().forEach(questionRepository::delete);
        survey.getSurveyQuestions().clear();
        surveyQuestions.forEach(survey::addQuestion);
        return repository.update(survey);
    }
}
