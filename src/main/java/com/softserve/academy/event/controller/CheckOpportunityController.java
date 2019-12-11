package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.CheckOpportunityDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping(value = "/testAccess")
@Slf4j
public class CheckOpportunityController {

    @GetMapping
    public String mailTest(@RequestParam(name = "token", required = true) String token){
        return "mailEnterPage";
    }

    @PostMapping(value = "/check")
    public String enterEmail(@RequestBody CheckOpportunityDTO checkOpportunityDTO){
        String token = checkOpportunityDTO.getToken();
        String email = checkOpportunityDTO.getEmail();
        String token_email = new String(Base64.getDecoder().decode(token)).split(";")[0];
        if (token_email.equals(email)){
            return "successPage";
        }else{
            return "errorPage";
        }
    }
}