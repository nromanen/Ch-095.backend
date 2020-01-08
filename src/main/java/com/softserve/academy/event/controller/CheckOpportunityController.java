package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.CheckOpportunityDTO;
import com.softserve.academy.event.dto.ContactSurveyDTO;
import com.softserve.academy.event.exception.SurveyAlreadyPassedException;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.Optional;

@Api(value = "/testAccess")
@RestController
@RequestMapping(value = "/testAccess")
@Slf4j
public class CheckOpportunityController {
    private final SurveyContactConnectorService surveyContactConnectorService;
    private final ContactService contactService;

    @Autowired
    public CheckOpportunityController(SurveyContactConnectorService surveyContactConnectorService, ContactService contactService) {
        this.surveyContactConnectorService = surveyContactConnectorService;
        this.contactService = contactService;
    }

    @ApiOperation(value = "Mail verification")
    @GetMapping(value = "/{token}")
    public ResponseEntity<String> mailTest(@PathVariable(name = "token") String token){
        String[] res;
        try{
            res =new String(Base64.getDecoder().decode(token)).split(";");
        }catch (Exception e){
            return new ResponseEntity<>("Sorry, but you can`t pass survey by this link", HttpStatus.BAD_REQUEST);
        }
        Optional<Long> longOptional = contactService.getIdByEmail(res[0]);
        if (longOptional.isPresent()){
            try {
                if (surveyContactConnectorService.isEnable(longOptional.get(), Long.valueOf(res[1]))){
                    return ResponseEntity.ok(token);
                }
            } catch (SurveyAlreadyPassedException e) {
                return new ResponseEntity<>("Sorry, but you have already passed this survey", HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>("Sorry, but you can`t pass survey by this link", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);

    }

    @ApiOperation(value = "Check e-mail")
    @PostMapping(value = "/check")
    public ResponseEntity<ContactSurveyDTO> enterEmail(@RequestBody CheckOpportunityDTO checkOpportunityDTO){
        String[] strings  = ";".split(new String(Base64.getDecoder().decode(checkOpportunityDTO.getToken())));
        final ContactSurveyDTO dto = new ContactSurveyDTO(strings[0], Long.valueOf(strings[1]));
        if (strings[0].equals(checkOpportunityDTO.getEmail())){
            return ResponseEntity.ok(dto);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
