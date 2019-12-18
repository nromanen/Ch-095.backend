package com.softserve.academy.event.service.mapper;

import com.softserve.academy.event.dto.AnswerDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.service.db.ContactService;
import com.softserve.academy.event.service.db.QuestionService;
import org.mapstruct.ObjectFactory;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AnswerMapperResolver {

    private ContactService contactService;
    private QuestionService questionService;

    @Autowired
    public AnswerMapperResolver(ContactService contactService, QuestionService questionService) {
        this.contactService = contactService;
        this.questionService = questionService;
    }

    @ObjectFactory
        public SurveyAnswer resolve(AnswerDTO dto, @TargetType Class<SurveyAnswer> type) {
        Optional<Contact> optionalContact = contactService.findFirstById(dto.getContactId());
        Optional<SurveyQuestion> optionalSurveyQuestion = questionService.findFirstById(dto.getQuestionId());
        SurveyAnswer answer = new SurveyAnswer();
        if(optionalContact.isPresent() && optionalSurveyQuestion.isPresent()){
            answer.setContact(optionalContact.get());
            answer.setQuestion(optionalSurveyQuestion.get());
        }
        return answer;
    }
}
