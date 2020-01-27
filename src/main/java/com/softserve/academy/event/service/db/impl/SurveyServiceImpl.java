package com.softserve.academy.event.service.db.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.*;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import com.softserve.academy.event.entity.enums.SurveyStatus;
import com.softserve.academy.event.entity.enums.SurveyType;
import com.softserve.academy.event.exception.SurveyNotFound;
import com.softserve.academy.event.exception.UserNotFound;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.repository.UserRepository;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.service.mapper.SaveQuestionMapper;
import com.softserve.academy.event.service.mapper.SurveyMapper;
import com.softserve.academy.event.util.DuplicateSurveySettings;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.softserve.academy.event.util.SecurityUserUtil.getCurrentUserEmail;

@Service
@Transactional
@Slf4j
@PropertySource("classpath:application.properties")
public class SurveyServiceImpl implements SurveyService {

    @Value("${image.upload.dir}")
    private String imageUploadDir;


    private final UserRepository userRepository;
    private final UserService userService;
    private final SurveyRepository repository;
    private final QuestionRepository questionRepository;
    private final SurveyMapper mapper;
    private final QuestionService questionService;
    private final SaveQuestionMapper saveQuestionMapper;

    @Autowired
    public SurveyServiceImpl(UserRepository userRepository, SurveyRepository repository,
                             UserService userService, QuestionRepository questionRepository,
                             SurveyMapper mapper, QuestionService questionService, SaveQuestionMapper saveQuestionMapper) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.repository = repository;
        this.questionRepository = questionRepository;
        this.mapper = mapper;
        this.questionService = questionService;
        this.saveQuestionMapper = saveQuestionMapper;
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
    public Survey saveSurveyWithQuestions(SaveSurveyDTO saveSurveyDTO) throws JsonProcessingException {
        Survey survey = saveQuestionMapper.toSurvey(saveSurveyDTO);
        if ("MANAGER".equals(getRole())) {
            survey.setStatus(SurveyStatus.TEMPLATE);
        }
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        for (SurveyQuestionDTO question : saveSurveyDTO.getQuestions()) {
            surveyQuestions.add(saveQuestionMapper.toEntity(question));
        }
        String email = userService.getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFound::new);
        survey.setUser(user);
        surveyQuestions.forEach(survey::addQuestion);
        return repository.save(survey);
    }

    @Override
    public Survey updateSurvey(Long surveyId, SaveSurveyDTO saveSurveyDTO) throws JsonProcessingException {
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        for (SurveyQuestionDTO questionDTO : saveSurveyDTO.getQuestions()) {
            surveyQuestions.add(saveQuestionMapper.toEntity(questionDTO));
        }
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

    @Override
    public ResponseEntity<EditSurveyDTO> loadSurvey(Long surveyId) throws IOException {
        List<SurveyQuestion> questions = questionService.findBySurveyId(surveyId);
        List<EditSurveyQuestionDTO> editSurveyQuestionsDTO = new ArrayList<>();
        savePhotoInEditSurveyDTO(questions, editSurveyQuestionsDTO);
        Survey survey = findFirstById(surveyId).orElseThrow(SurveyNotFound::new);
        EditSurveyDTO editSurveyDTO = saveQuestionMapper.toEditSurveyDTO(survey, editSurveyQuestionsDTO);
        return new ResponseEntity<>(editSurveyDTO, HttpStatus.OK);
    }

    private void savePhotoInEditSurveyDTO(List<SurveyQuestion> questions, List<EditSurveyQuestionDTO> editSurveyQuestionsDTO) throws IOException {
        for (SurveyQuestion question : questions) {
            EditSurveyQuestionDTO editSurveyQuestionDTO = saveQuestionMapper.toEditSurveyQuestionDTO(question);
            if (question.getType().equals(SurveyQuestionType.CHECKBOX_PICTURE) ||
                    question.getType().equals(SurveyQuestionType.RADIO_PICTURE)) {
                for (String filename : editSurveyQuestionDTO.getChoiceAnswers()) {
                    editSurveyQuestionDTO.getUploadingPhotos().add(QuestionServiceImpl.getPhotoAsEncodeStrByFilename(filename));
                }
            }
            editSurveyQuestionsDTO.add(editSurveyQuestionDTO);
        }
    }

    private String getRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .findFirst().orElseThrow(UserNotFound::new).toString();
    }


}
