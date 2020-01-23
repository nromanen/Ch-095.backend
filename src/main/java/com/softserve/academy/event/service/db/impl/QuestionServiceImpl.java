package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.entity.Respondent;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.repository.AnswerRepository;
import com.softserve.academy.event.repository.QuestionRepository;
import com.softserve.academy.event.service.db.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@PropertySource("classpath:application.properties")
public class QuestionServiceImpl implements QuestionService {

    @Value("${image.upload.dir}")
    private static String imageUploadDir;

    private final QuestionRepository questionRepository;
    private final RespondentService respondentService;
    private final AnonymService anonymService;
    private final AnswerService answerService;
    private final SurveyContactConnectorService surveyContactService;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository, AnonymService anonymService, RespondentService respondentService, AnonymService anonymService1, AnswerService answerService, SurveyContactConnectorService surveyContactService) {
        this.questionRepository = questionRepository;
        this.respondentService = respondentService;
        this.anonymService = anonymService1;
        this.answerService = answerService;
        this.surveyContactService = surveyContactService;
    }

    @Override
    public Optional<SurveyQuestion> findFirstById(Long id) {
        return questionRepository.findFirstById(id);
    }

    @Override
    public List<SurveyQuestion> findAll() {
        return questionRepository.findAll();
    }

    @Override
    public SurveyQuestion save(SurveyQuestion entity) {
        return questionRepository.save(entity);
    }

    @Override
    public SurveyQuestion update(SurveyQuestion object) {
        return questionRepository.update(object);
    }

    @Override
    public void delete(SurveyQuestion entity) {
        questionRepository.delete(entity);
    }

    @Override
    public List<SurveyQuestion> findBySurveyId(Long surveyId) {
        return questionRepository.findBySurveyId(surveyId);
    }

    @Override
    public List<SurveyAnswer> saveAnswers(List<SurveyAnswer> answers) {

        Respondent respondent = respondentService.save(anonymService.save(answers.get(0).getValue()));
        answers.remove(0);
        answers.forEach(answer -> answer.setRespondent(respondent));
        answerService.saveAll(answers);

        return answers;
    }

    @Override
    public List<SurveyAnswer> saveAnswers(List<SurveyAnswer> answers, SurveyContact surveyContact) {

        surveyContact.setCanPass(false);
        surveyContactService.update(surveyContact);
        Respondent respondent = respondentService.save(surveyContact.getContact());
        answers.forEach(answer -> answer.setRespondent(respondent));
        answerService.saveAll(answers);

        return answers;
    }

    public static String getPhotoAsEncodeStrByFilename(String imageUploadDir, String filename) throws IOException {
        Path path = Paths.get(imageUploadDir,filename);
        byte[] arr = Files.readAllBytes(path);
        return Base64.getEncoder().encodeToString(arr);
    }


}
