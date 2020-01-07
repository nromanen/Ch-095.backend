package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.OneQuestionSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyQuestion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {ObjectMapper.class, Collectors.class,
         Arrays.class, Objects.class})
@Service
public interface SeparatelyStatisticMapper {

    default Set<QuestionsSeparatelyStatisticDTO> toSetQuestionsDTO(Survey survey) {
        if (survey == null) {
            return new HashSet<>();
        }

        Set<QuestionsSeparatelyStatisticDTO> hashSet = new HashSet<>();

        for (Contact contact : survey.getContacts()) {
            hashSet.add(toQuestionsDTO(contact, survey));
        }
        return hashSet;
    }

    @Mapping(target = "questionDTOS",
            expression = "java(toSetOneQuestionDTO(survey.getSurveyQuestions(),contact))")
    @Mapping(target = "email", expression = "java(contact.getEmail())")
    QuestionsSeparatelyStatisticDTO toQuestionsDTO(Contact contact, Survey survey);

    default Set<OneQuestionSeparatelyStatisticDTO> toSetOneQuestionDTO(
            List<SurveyQuestion> surveyQuestions, Contact contact) {
        if (surveyQuestions == null || contact == null) {
            return new HashSet<>();
        }

        Set<OneQuestionSeparatelyStatisticDTO> hashSet = new HashSet<>();

        for (SurveyQuestion surveyQuestion : surveyQuestions) {
            try {
                hashSet.add(toOneQuestionDTO(surveyQuestion, contact));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return hashSet;
    }

    @Mapping(target = "choiceAnswers", expression = "java(new ObjectMapper().readValue(" +
            "surveyQuestion.getChoiceAnswers(),String[].class))")
    @Mapping(target = "answer",
            expression = "java(transformationToAnswer(surveyQuestion,contact))")
    OneQuestionSeparatelyStatisticDTO toOneQuestionDTO(SurveyQuestion surveyQuestion, Contact contact)
            throws JsonProcessingException;

    default List<String> transformationToAnswer
            (SurveyQuestion surveyQuestion, Contact contact) {
         return surveyQuestion.getSurveyAnswers().stream().map(answer -> {
             if (answer.getContact().equals(contact)) {
                 try {
                     return new ObjectMapper().readValue(answer.getValue(), String[].class);
                 } catch (JsonProcessingException e) {
                     e.printStackTrace();
                 }
             }
             return null;
         })
                 .filter(Objects::nonNull)
                 .flatMap(Arrays::stream)
                 .collect(Collectors.toList());
    }

}
