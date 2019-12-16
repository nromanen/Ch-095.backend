package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.CheckOpportunityDTO;
import com.softserve.academy.event.response.ServerResponse;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.SurveyContactConnectorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public CheckOpportunityController(SurveyContactConnectorService surveyContactConnectorService, ContactService contactService){
        this.surveyContactConnectorService = surveyContactConnectorService;
        this.contactService = contactService;
    }

    @ApiOperation(value = "Mail verification")
    @GetMapping(value = "/{token}")
    public ServerResponse<String> mailTest(@PathVariable(name = "token") String token){
        String[] res = new String(Base64.getDecoder().decode(token)).split(";");
        Optional<Long> longOptional = contactService.getIdByEmail(res[0]);
        if (longOptional.isPresent()){
            if (surveyContactConnectorService.isEnable(longOptional.get(), Long.valueOf(res[1])))
                return ServerResponse.success(token);
        }
        return ServerResponse.from("", HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "")
    @PostMapping(value = "/check")
    public ServerResponse<String> enterEmail(@RequestBody CheckOpportunityDTO checkOpportunityDTO){
        String token_email = new String(Base64.getDecoder().decode(checkOpportunityDTO.getToken())).split(";")[0];
        if (token_email.equals(checkOpportunityDTO.getEmail())){
            return ServerResponse.success(checkOpportunityDTO.getToken());
        }else{
            return ServerResponse.from("", HttpStatus.BAD_REQUEST);
        }
    }
}