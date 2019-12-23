package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class SurveyServiceImpl implements SurveyService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final SurveyRepository repository;


    @Autowired
    public SurveyServiceImpl(UserRepository userRepository, SurveyRepository repository, UserService userService) {
        this.userRepository = userRepository;
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public Page<Survey> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }


    @Override
    public Page<Survey> findAllByPageableAndStatus(Pageable pageable, String status) {
        return repository.findAllByPageableAndStatus(pageable, status);
    }

    @Override
    public Page<Survey> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters) {
        return repository.findAllFiltered(pageable,
                Objects.nonNull(filters) ? filters :
                        Collections.singletonMap("surveyStatusField",
                                Collections.singletonMap("status", SurveyStatus.TEMPLATE.getNumber()))
        );
    }

    @Override
    public HttpStatus updateTitle(Long id, String title) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        repository.update(survey);
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus updateStatus(Long id, SurveyStatus status) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(RuntimeException::new);
        survey.setStatus(status);
        repository.update(survey);
        return HttpStatus.OK;
    }

    @Override
    public Survey duplicateSurvey(DuplicateSurveySettings settings) {
        Survey survey = repository.findFirstById(settings.getId())
                .orElseThrow(RuntimeException::new);
        repository.detach(survey);
        survey.setId(null);
        survey.setCreationDate(new Date());
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
    public Survey saveSurveyWithQuestions(Survey survey, List<SurveyQuestion> surveyQuestions) {
        Long userID = userService.getAuthenicationId().get();
        User user  = userRepository.findFirstById(userID).get();
        survey.setUser(user);
        surveyQuestions.stream().forEach(x->survey.addQuestion(x));
        Survey savedSurvey = repository.save(survey);
        return savedSurvey;
    }
}
