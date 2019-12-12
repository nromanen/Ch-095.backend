package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.repository.impl.SurveyRepositoryImpl;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

//TODO
//CHECK ALL CHANGES IN CLASSES
@Service
public class SurveyServiceImpl extends BasicServiceImpl<Survey, Long> implements SurveyService {

    private final SurveyRepositoryImpl repository;
    private final UserService userService;

    public SurveyServiceImpl(SurveyRepositoryImpl repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional
    public Page<SimpleSurveyDTO> findAll(Pageable pageable) {
        Page<Survey> page = repository.findAll(pageable);
        return new Page<>(
                page.getItems().stream()
                        .map(SimpleSurveyDTO::toSimpleUser)
                        .collect(Collectors.toList()),
                pageable); // convert to dto
    }

    @Transactional
    public HttpStatus updateTitle(Long id, String title) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        update(survey);
        return HttpStatus.OK;
    }

    @Transactional
    public SimpleSurveyDTO duplicateSurvey(Long id) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setId(null);
        detach(survey);
        save(survey);
        return SimpleSurveyDTO.toSimpleUser(survey);
    }

    @Transactional
    public String setTitleForSurvey(Long id, String title) {
        Survey survey = findFirstById(id).orElseThrow(RuntimeException::new);
        survey.setTitle(title);
        update(survey);
        return survey.getTitle();
    }

}
