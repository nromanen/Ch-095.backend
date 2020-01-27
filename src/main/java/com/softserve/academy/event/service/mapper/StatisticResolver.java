package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyContact;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import com.softserve.academy.event.exception.EncodePhotoException;
import com.softserve.academy.event.exception.IncorrectDataDB;
import com.softserve.academy.event.service.db.impl.QuestionServiceImpl;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StatisticResolver {


    static List<String> parseChoiceAnswers(SurveyQuestion surveyQuestion){
        try {
            List<String> choiceAnswers =  Arrays.asList(new ObjectMapper().readValue(
                    surveyQuestion.getChoiceAnswers(),String[].class));
            if (surveyQuestion.getType().equals(SurveyQuestionType.CHECKBOX_PICTURE)
                    || surveyQuestion.getType().equals(SurveyQuestionType.RADIO_PICTURE)){
                return encodeImageList(choiceAnswers);
            }
            return choiceAnswers;
        } catch (JsonProcessingException e) {
            throw new IncorrectDataDB(String.format("ChoiceAnswers = %s incorrect in SurveyQuestion with id = %s",
                    surveyQuestion.getChoiceAnswers(),surveyQuestion.getId()));
        }
    }

    static List<String> encodeImageList(List<String> imageNames){
        List<String> encodeImages = new ArrayList<>();
        imageNames.forEach(choiceAnswer -> {
            try {
                encodeImages.add(QuestionServiceImpl.getPhotoAsEncodeStrByFilename(choiceAnswer));
            } catch (IOException e) {
                throw new EncodePhotoException();
            }
        });

        return encodeImages;
    }

    static List<List<String>> parseAllAnswers(Set<SurveyAnswer> surveyAnswerSet) {
        return surveyAnswerSet.stream()
                .map(surveyAnswer -> {
                    try {
                        List<String> answers ;
                        answers = Arrays.asList(new ObjectMapper()
                                .readValue(surveyAnswer.getValue(), String[].class));
                        if (surveyAnswer.getQuestion().getType().equals(SurveyQuestionType.CHECKBOX_PICTURE)
                                || surveyAnswer.getQuestion().getType().equals(SurveyQuestionType.RADIO_PICTURE)){
                            return encodeImageList(answers);
                        }
                        return answers;
                    } catch (JsonProcessingException e) {
                        throw new IncorrectDataDB(String.format("Value = %s incorrect in surveyAnswer with id = %s",
                                surveyAnswer.getValue(),surveyAnswer.getId()));
                    }
                }).collect(Collectors.toList());
    }

    static List<String> parseAnswer
            (Set<SurveyAnswer> surveyAnswerSet, SurveyContact contact) {
        return surveyAnswerSet.stream()
                .filter(surveyAnswer ->surveyAnswer.getRespondent().getContact().equals(contact.getContact()))
                .map(answer -> {
                    try {
                        List<String> answers ;
                        answers = Arrays.asList(new ObjectMapper().readValue(answer.getValue(), String[].class));
                        if (answer.getQuestion().getType().equals(SurveyQuestionType.CHECKBOX_PICTURE)
                                || answer.getQuestion().getType().equals(SurveyQuestionType.RADIO_PICTURE)){
                            return encodeImageList(answers).toArray(new String[0]);
                        }
                        return answers.toArray(new String[0]);
                    } catch (JsonProcessingException e) {
                        throw new IncorrectDataDB(String.format("Value = %s incorrect in surveyAnswer with id = %s",
                                answer.getValue(),answer.getId()));
                    }
                })
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    private StatisticResolver(){}

}
