package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.repository.impl.SurveyRepositoryImpl;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class SurveyServiceImpl extends BasicServiceImpl<Survey, Long> implements SurveyService {

    private final SurveyRepositoryImpl repository;
    private UserRepository userRepository;
    private QuestionRepository questionRepository;

    public SurveyServiceImpl(SurveyRepositoryImpl repository, UserRepository userRepository, QuestionRepository questionRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Page<SimpleSurveyDTO> findAll(Pageable pageable) {
        Page<Survey> page = repository.findAll(pageable);
        return new Page<>(
                page.getItems().stream()
                        .map(SimpleSurveyDTO::toSimpleUser)
                        .collect(Collectors.toList()),
                pageable);
    }

    @Override
    public Page<SimpleSurveyDTO> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters) {
        Page<Survey> page = repository.findAllFiltered(pageable,
                Objects.nonNull(filters) ? filters :
                        Collections.singletonMap("surveyStatusField",
                                Collections.singletonMap("status", SurveyStatus.TEMPLATE.getNumber()))
        );
        return new Page<>(
                page.getItems().stream()
                        .map(SimpleSurveyDTO::toSimpleUser)
                        .collect(Collectors.toList()),
                pageable); // convert to dto
    }

    @Override
    public HttpStatus updateTitle(Long id, String title) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        update(survey);
        return HttpStatus.OK;
    }

    @Override
    public SimpleSurveyDTO duplicateSurvey(Long id) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setId(null);
        detach(survey);
        save(survey);
        return SimpleSurveyDTO.toSimpleUser(survey);
    }

    @Override
    public String setTitleForSurvey(Long id, String title) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        update(survey);
        return survey.getTitle();
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
