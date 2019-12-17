package com.softserve.academy.event.controller;

import com.softserve.academy.event.dto.QuestionDTO;
import com.softserve.academy.event.dto.SurveyContactDTO;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.service.db.QuestionService;
import com.softserve.academy.event.service.mapper.QuestionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("question")
public class QuestionController {
//
//<<<<<<< Updated upstream
//=======
//    @Autowired
//    private ContactService contactService;
//    @Autowired
//    private AnswerService answerService;
//    @Autowired
//    private SurveyContactConnectorService sccService;
//    @Autowired
//    private AnswerMapper answerMapper;
//>>>>>>> Stashed changes
//    @Autowired
//    private QuestionService questionService;
//    @Autowired
//    private QuestionMapper questionMapper;
//
//    @GetMapping
//    public ResponseEntity<SurveyContactDTO> startSurvey(@RequestParam Long surveyId, @RequestParam String contactEmail){
//        List<SurveyQuestion> questions = questionService.findBySurveyId(surveyId);
//        List<QuestionDTO> questionsDTO = questionMapper.listQuestionToDTO(questions);
//        SurveyContactDTO dto = new SurveyContactDTO();
//        dto.setContactEmail(contactEmail);
//        dto.setSurveyId(surveyId);
//        dto.setQuestions(questionsDTO);
//<<<<<<< Updated upstream
//        return dto;
//    }
//=======
//        if(questionsDTO.isEmpty())
//            return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
//        return new ResponseEntity<>(dto, HttpStatus.OK);
//    }

//    @ApiOperation(value = "Save answers to the database")
//    @PostMapping
//    public ResponseEntity<String> addAnswers(ContactResponseDTO contactResponseDTO){
//        String result;
//        Optional<Long> contactId = contactService.getIdByEmail(contactResponseDTO.getContactEmail());
//        if(!contactId.isPresent()) {
//            result = "missing email data";
//            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//        }
//        Optional <SurveyContact> surveyContactConnector =
//                sccService.findByContactAndSurvey(contactId.get(), contactResponseDTO.getSurveyId());
//        if(!surveyContactConnector.isPresent()){
//            result = "mail is not in the invite list";
//            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//        }
//
//        surveyContactConnector.get().setEnable(false);
//        sccService.update(surveyContactConnector.get());
//        contactResponseDTO.getAnswers().stream()
//                .peek(answerDTO -> answerDTO.setContactId(contactId.get()))
//                .map(answerMapper::toEntity)
//                .forEach(answerService::save);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//>>>>>>> Stashed changes
}