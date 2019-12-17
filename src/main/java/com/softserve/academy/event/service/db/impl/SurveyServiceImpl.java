package com.softserve.academy.event.service.db.impl;

import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.repository.SurveyRepository;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class SurveyServiceImpl extends BasicServiceImpl<Survey, Long> implements SurveyService{
    private final SurveyRepository repository;

    @Autowired
    public SurveyServiceImpl(SurveyRepository repository){
        this.repository = repository;
    }
//    @Override
//    public boolean getEventById(String surveyId) {
//        return repository.getEventById(surveyId);
//    }

    @Override
    public void save(Set<Contact> contactSet) {
        repository.save(contactSet);
    }

    @Override
    public Page<SimpleSurveyDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<SimpleSurveyDTO> findAllFiltered(Pageable pageable, Map<String, Map<String, Object>> filters) {
        return null;
    }

    @Override
    public HttpStatus updateTitle(Long id, String title) {
        return null;
    }

    @Override
    public SimpleSurveyDTO duplicateSurvey(Long id) {
        return null;
    }

    @Override
    public String setTitleForSurvey(Long id, String title) {
        return null;
    }

}
