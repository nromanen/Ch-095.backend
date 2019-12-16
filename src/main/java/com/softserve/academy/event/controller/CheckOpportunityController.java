package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.CheckOpportunityDTO;
import com.softserve.academy.event.dto.ContactSurveyDTO;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping(value = "/testAccess")
@CrossOrigin(origins = "http://localhost:4200")
@Slf4j
public class CheckOpportunityController {
    private final SurveyContactConnectorService surveyContactConnectorService;
    private final ContactService contactService;

    @Autowired
    public CheckOpportunityController(SurveyContactConnectorService surveyContactConnectorService, ContactService contactService){
        this.surveyContactConnectorService = surveyContactConnectorService;
        this.contactService = contactService;
    }

    @GetMapping(value = "/{token}")
    public ResponseEntity<String> mailTest(@PathVariable(name = "token") String token){
        String[] res = new String(Base64.getDecoder().decode(token)).split(";");
        Optional<Long> longOptional = contactService.getIdByEmail(res[0]);
        if (longOptional.isPresent()){
            if (surveyContactConnectorService.isEnable(longOptional.get(), Long.valueOf(res[1]))){
//                return new ResponseEntity<>(token, HttpStatus.OK);
                return ResponseEntity.ok(token);
            }
            return new ResponseEntity<>("", HttpStatus.GONE);
        }
        return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);

    }

    @PostMapping(value = "/check")
    public ResponseEntity<ContactSurveyDTO> enterEmail(@RequestBody CheckOpportunityDTO checkOpportunityDTO){
        String[] strings  = new String(Base64.getDecoder().decode(checkOpportunityDTO.getToken())).split(";");
        final ContactSurveyDTO dto = new ContactSurveyDTO(strings[0], Long.valueOf(strings[1]));
        if (strings[0].equals(checkOpportunityDTO.getEmail())){
            return ResponseEntity.ok(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}