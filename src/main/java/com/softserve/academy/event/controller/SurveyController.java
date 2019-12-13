package com.softserve.academy.event.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.annotation.PageableDefault;
import com.softserve.academy.event.dto.SaveSurveyDTO;
import com.softserve.academy.event.dto.SimpleSurveyDTO;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.User;
import com.softserve.academy.event.response.ServerResponse;
import com.softserve.academy.event.service.db.SurveyService;
import com.softserve.academy.event.service.db.UserService;
import com.softserve.academy.event.util.Page;
import com.softserve.academy.event.util.Pageable;
import com.softserve.academy.event.util.Sort;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@RestController
@RequestMapping("survey")
public class SurveyController {

    private final SurveyService service;
    private final UserService userService;

    public SurveyController(SurveyService service, UserService userService) {
        this.service = service;
        this.userService = userService;
    }

    @GetMapping
    public ServerResponse<Page<SimpleSurveyDTO>> findAllSurveys(
            @PageableDefault(sort = {"creationDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ServerResponse.success(service.findAll(pageable));
    }

    @PostMapping
    public ServerResponse<SimpleSurveyDTO> duplicateSurvey(Long id) {
        return ServerResponse.success(service.duplicateSurvey(id));
    }

    @PutMapping
    public ServerResponse<String> updateTitle(Long id, String title) {
        return ServerResponse.success(service.setTitleForSurvey(id, title));
    }

    @DeleteMapping
    public ServerResponse<Long> deleteSurvey(Survey survey) {
        service.delete(survey);
        return ServerResponse.success(survey.getId());
    }

    @PostMapping(value = "/save")
    public Survey saveSurvey(@RequestBody SaveSurveyDTO saveSurveyDTO) throws IOException {
        Survey survey = new Survey();
        survey.setTitle(saveSurveyDTO.getTitle());
        if (userService.findFirstById(saveSurveyDTO.getUserID()).isPresent()) {
            User user = userService.findFirstById(saveSurveyDTO.getUserID()).get();
            survey.setUser(user);
        } else {
//            return ServerResponse.from(saveSurveyDTO, HttpStatus.BAD_REQUEST);
            return survey;
        }
//        return ServerResponse.success(service.save(survey));
        return service.save(survey);
    }

    private SaveSurveyDTO getSaveSurveyDTO() throws IOException {
        File file = new File(pathToFileWithSurveyInJson("inputJson"));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, SaveSurveyDTO.class);
    }

    private String pathToFileWithSurveyInJson(String fileName) throws IOException {
        InputStream propertiesFile = FileUploadController.class.getClassLoader().getResourceAsStream("application.properties");
        Properties properties = new Properties();
        properties.load(propertiesFile);
        return properties.getProperty("imageUploadDir") + File.separator + fileName + ".json";
    }

}
