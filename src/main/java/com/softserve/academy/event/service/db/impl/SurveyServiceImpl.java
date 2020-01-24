package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.entity.enums.SurveyType;
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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public SurveyServiceImpl(UserRepository userRepository, SurveyRepository repository,
                             UserService userService, QuestionRepository questionRepository, SurveyMapper mapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.repository = repository;
        this.questionRepository = questionRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Page<SurveyDTO> findAllByPageableAndStatus(Pageable pageable, String status) {
        Page<Survey> surveysPage;

        if ("TEMPLATE".equals(status)) {
            surveysPage = repository.findSurveysByTemplateStatus(pageable,
                    SurveyStatus.valueOf(status));
        } else if (Objects.nonNull(status) && status.length() > 0) {
            surveysPage = repository.findAllByPageableAndStatusAndUserEmail(pageable,
                    SurveyStatus.valueOf(status), getCurrentUserEmail());
        } else {
            surveysPage = repository.findAllByPageableAndUserEmail(pageable, getCurrentUserEmail());
        }
        return new Page<>(
                surveysPage.getItems()
                        .stream()
                        .map(this::surveyToSurveyDto)
                        .collect(Collectors.toList()),
                surveysPage.getPageable()
        );
    }

    private SurveyDTO surveyToSurveyDto(Survey survey) {
        return mapper.toDTO(
                survey,
                survey.getSurveyContacts()
                        .stream()
                        .filter(SurveyContact::isCanPass)
                        .count(),
                (long) survey.getSurveyContacts().size());
    }

    @Override
    public void updateTitle(Long id, String title) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(SurveyNotFound::new);
        survey.setTitle(title);
        repository.update(survey);
    }

    @Override
    public void updateStatus(Long id, SurveyStatus status) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(SurveyNotFound::new);
        survey.setStatus(status);
        repository.update(survey);
    }

    @Override
    public long duplicate(DuplicateSurveySettings settings) {
        Long id = userService.getAuthenticationId().orElseThrow(UserNotFound::new);
        return repository.cloneSurvey(settings, id)
                .orElseThrow(SurveyNotFound::new)
                .longValue();
    }

    @Override
    public void disable(Long id) {
        Survey survey = repository.findFirstById(id)
                .orElseThrow(SurveyNotFound::new);
        survey.setActive(false);
        repository.update(survey);
    }

    @Override
    public void delete(Long id) {
        Survey survey = repository.findFirstByIdForNormPeople(id)
                .orElseThrow(SurveyNotFound::new);
        repository.delete(survey);
    }

    @Override
    public Optional<Survey> findFirstById(long surveyId) {
        return repository.findFirstById(surveyId);
    }

    @Override
    public Optional<Survey> findFirstByIdForNormPeople(long surveyId) {
        return repository.findFirstByIdForNormPeople(surveyId);
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

    @Override
    public boolean isCommonWithIdAndNameExist(Long id, String name) {
        Optional<Survey> surveyOptional = findFirstByIdForNormPeople(id);
        if (!surveyOptional.isPresent()){
            return false;
        }
        Survey survey = surveyOptional.get();
        return survey.getType().equals(SurveyType.COMMON) && survey.getTitle().equals(name);
    }

}
