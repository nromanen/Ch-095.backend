package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.dto.*;
import com.softserve.academy.event.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {ObjectMapper.class, Collectors.class,
        Collection.class, Arrays.class, Objects.class})
@Service
public interface StatisticMapper {

    @Mapping(target = "questionDTOS", source = "surveyQuestions")
    SurveyGeneralStatisticDTO toSurveyGeneralDTO(Survey survey);

    @Mapping(target = "choiceAnswers", expression = "java(new ObjectMapper().readValue(" +
            "surveyQuestion.getChoiceAnswers(),String[].class))")
    @Mapping(target = "answers", expression = "java(surveyQuestion.getSurveyAnswers().stream().map(surveyAnswer -> {" +
            "            try {" +
            "                return Arrays.asList(new ObjectMapper().readValue( surveyAnswer.getValue(),String[].class));" +
            "            } catch (JsonProcessingException e) {" +
            "               return null;" +
            "            }" +
            "        }).collect(Collectors.toList()))")
    QuestionStatisticDTO toQuestionDTO(SurveyQuestion surveyQuestion) throws JsonProcessingException;

    Set<QuestionStatisticDTO> listQuestionStatisticToDTO(Set<SurveyQuestion> surveyQuestions);





    @Mapping(target = "contactDTOS", expression = "java(listQuestionContactsDTO(survey.getContacts(),survey))")
    SurveyEachStatisticDTO toSurveyEachDTO(Survey survey);


    @Mapping(target = "questionDTOS", expression = "java(listQuestionforContactStatisticToDTO(survey.getSurveyQuestions(),contact))")
    @Mapping(target = "email", expression = "java(contact.getEmail())")
    QuestionContactsStatisticDTO toQuestionContactsDTO(Contact contact, Survey survey);

    @Named("listQuestionContactsDTO")
    default HashSet<QuestionContactsStatisticDTO> listQuestionContactsDTO(Set<Contact> contacts, Survey survey){
        if ( contacts == null || survey == null ) {
            return new HashSet<>();
        }

        HashSet<QuestionContactsStatisticDTO> hashSet = new HashSet<>();

        for ( Contact contact :contacts ) {
            hashSet.add(toQuestionContactsDTO(contact,survey));
        }
        return hashSet;
    }

    @Named("listQuestionforContactStatisticToDTO")
    default HashSet<QuestionforContactStatisticDTO> listQuestionforContactStatisticToDTO(
            Set<SurveyQuestion> surveyQuestions, Contact contact){
        if ( surveyQuestions == null || contact == null ) {
            return new HashSet<>();
        }

        HashSet<QuestionforContactStatisticDTO> hashSet = new HashSet<>();

        for ( SurveyQuestion surveyQuestion : surveyQuestions) {
            try {
                hashSet.add(questionforContactStatisticToDTO(surveyQuestion,contact));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return hashSet;
    }


    @Mapping(target = "choiceAnswers", expression = "java(new ObjectMapper().readValue(" +
            "surveyQuestion.getChoiceAnswers(),String[].class))")
    @Mapping(target = "answers",
            expression = "java(surveyQuestion.getSurveyAnswers().stream()" +
                    ".map(answer -> {if(answer.getContact().equals(contact)) " +
                    "{" +
                    "  try {" +
                    "   return new ObjectMapper().readValue(answer.getValue(),String[].class);" +
                    "  } catch (JsonProcessingException e) {" +
                    "     e.printStackTrace();" +
                    "     }" +
                    " }" +
                    "      return null;" +
                    "}).filter(Objects::nonNull).flatMap(Arrays::stream).collect(Collectors.toList()))")
    QuestionforContactStatisticDTO questionforContactStatisticToDTO(SurveyQuestion surveyQuestion, Contact contact)
                                                            throws JsonProcessingException;

}
