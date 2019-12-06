package com.softserve.academy.event.controller;

import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.response.ServerResponse;
import com.softserve.academy.event.service.SurveyService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("survey")
public class SurveyController {

    private final SurveyService service;

    public SurveyController(SurveyService service) {
        this.service = service;
    }

    @GetMapping
    public ServerResponse<Page<SimpleSurveyDTO>> findAllSurveys(@PageableDefault Pageable pageable) {
        return ServerResponse.success(service.findAll(pageable));
    }

}
