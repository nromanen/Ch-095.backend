package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.OneQuestionSeparatelyStatisticDTO;
import com.softserve.academy.event.dto.QuestionsSeparatelyStatisticDTO;
import com.softserve.academy.event.entity.Contact;
import com.softserve.academy.event.entity.Survey;
import com.softserve.academy.event.entity.SurveyAnswer;
import com.softserve.academy.event.entity.SurveyQuestion;
import com.softserve.academy.event.entity.enums.SurveyQuestionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SeparatelyStatisticMapperTest {

    private  static Survey survey;
    private SeparatelyStatisticMapper separatelyStatisticMapper;

    @BeforeAll
    static void initializeDTO(){
        survey = new Survey();
        List<Contact> contacts = new ArrayList<>();

        Contact contact = new Contact();
        contact.setId(1L);
        contact.setEmail("test1@gmail.com");
        contacts.add(contact);

        survey.setContacts(new HashSet<>(contacts));
        List<SurveyQuestion> questionList = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setChoiceAnswers("[]");
        question.setIndex(1);
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setQuestion("What's your name?");
        Set<SurveyAnswer> answerSet = new HashSet<>();


        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(1L);
        surveyAnswer.setContact(contacts.get(0));
        surveyAnswer.setValue("[\"Vlad\"]");
        answerSet.add(surveyAnswer);


        question.setSurveyAnswers(answerSet);
        questionList.add(question);


        question = new SurveyQuestion();
        question.setChoiceAnswers("[\"1 a.m.\",\"1 p.m.\",\"3 p.m.\"]");
        question.setIndex(2);
        question.setType(SurveyQuestionType.RADIOBUTTON);
        question.setQuestion("What's time?");
        answerSet = new HashSet<>();


        surveyAnswer = new SurveyAnswer();
        surveyAnswer.setId(1L);
        surveyAnswer.setContact(contacts.get(0));
        surveyAnswer.setValue("[\"1 p.m.\"]");
        answerSet.add(surveyAnswer);

        question.setSurveyAnswers(answerSet);
        questionList.add(question);

        survey.setSurveyQuestions(questionList);
    }

    @BeforeEach
    void setUp() {
        separatelyStatisticMapper = Mappers.getMapper(SeparatelyStatisticMapper.class);
    }


    @ParameterizedTest
    @MethodSource("sourceToSetQuestionsDTO")
    void toSetQuestionsDTO(Survey survey,Set<QuestionsSeparatelyStatisticDTO> expected) {

        Set<QuestionsSeparatelyStatisticDTO> dto =
                separatelyStatisticMapper.toSetQuestionsDTO(survey);

        assertEquals(expected,dto);
    }

    @Test
    void toQuestionsDTOWithoutQuestions(){
        Survey survey = new Survey();
        survey.setTitle("Survey #1");
        Set<QuestionsSeparatelyStatisticDTO> dto =
                separatelyStatisticMapper.toSetQuestionsDTO(survey);
        assertTrue(dto.isEmpty());
    }

    @Test
    void toQuestionsDTOWithoutAnswers(){
        Survey survey = new Survey();
        Contact contact = new Contact();
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        survey.setContacts(contacts);
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setIndex(1);
        question.setChoiceAnswers("[]");
        question.setQuestion("");
        surveyQuestions.add(question);
        Set<SurveyAnswer> surveyAnswers = new HashSet<>();
        question.setSurveyAnswers(surveyAnswers);
        survey.setSurveyQuestions(surveyQuestions);
        assertDoesNotThrow(() -> separatelyStatisticMapper.toSetQuestionsDTO(survey));
    }

    @Test
    void ifChoiceAnswersIsNotCorrect(){
        Survey survey = new Survey();
        Contact contact = new Contact();
        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        survey.setContacts(contacts);
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setChoiceAnswers("adsdsa");
        surveyQuestions.add(question);
        survey.setSurveyQuestions(surveyQuestions);
        Set<QuestionsSeparatelyStatisticDTO> dtoSet =
                separatelyStatisticMapper.toSetQuestionsDTO(survey);
        dtoSet.forEach(value ->{
            assertTrue(value.getQuestionDTOS().isEmpty());
        });
    }

    static Stream<Arguments> sourceToSetQuestionsDTO(){

        Set<QuestionsSeparatelyStatisticDTO> expected
                = new HashSet<>();
        QuestionsSeparatelyStatisticDTO questions =
                new QuestionsSeparatelyStatisticDTO();
        questions.setEmail("test1@gmail.com");
        Set<OneQuestionSeparatelyStatisticDTO> oneQuestionSet = new HashSet<>();
        OneQuestionSeparatelyStatisticDTO oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(1);
        oneQuestion.setType(SurveyQuestionType.TEXTAREA);
        oneQuestion.setQuestion("What's your name?");
        oneQuestion.setChoiceAnswers(new String[0]);
        oneQuestion.setAnswer(Arrays.asList("Vlad"));
        oneQuestionSet.add(oneQuestion);

        oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(2);
        oneQuestion.setType(SurveyQuestionType.RADIOBUTTON);
        oneQuestion.setQuestion("What's time?");
        oneQuestion.setChoiceAnswers(new String[]{"1 a.m.","1 p.m.","3 p.m."});
        oneQuestion.setAnswer(Arrays.asList("1 p.m."));
        oneQuestionSet.add(oneQuestion);
        questions.setQuestionDTOS(oneQuestionSet);
        expected.add(questions);


        return Stream.of(Arguments.of(survey,expected));
    }

    @ParameterizedTest
    @MethodSource("sourceToQuestionsDTO")
    void toQuestionsDTO(Contact contact, Survey survey,
                        QuestionsSeparatelyStatisticDTO expected) {
        QuestionsSeparatelyStatisticDTO dto =
                separatelyStatisticMapper.toQuestionsDTO(contact,survey);

        assertEquals(expected,dto);
    }

    static Stream<Arguments> sourceToQuestionsDTO() {
        List<QuestionsSeparatelyStatisticDTO> expected
                = new ArrayList<>();
        QuestionsSeparatelyStatisticDTO questions =
                new QuestionsSeparatelyStatisticDTO();
        questions.setEmail("test1@gmail.com");
        Set<OneQuestionSeparatelyStatisticDTO> oneQuestionSet = new HashSet<>();
        OneQuestionSeparatelyStatisticDTO oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(1);
        oneQuestion.setType(SurveyQuestionType.TEXTAREA);
        oneQuestion.setQuestion("What's your name?");
        oneQuestion.setChoiceAnswers(new String[0]);
        oneQuestion.setAnswer(Arrays.asList("Vlad"));
        oneQuestionSet.add(oneQuestion);

        oneQuestion = new OneQuestionSeparatelyStatisticDTO();
        oneQuestion.setIndex(2);
        oneQuestion.setType(SurveyQuestionType.RADIOBUTTON);
        oneQuestion.setQuestion("What's time?");
        oneQuestion.setChoiceAnswers(new String[]{"1 a.m.","1 p.m.","3 p.m."});
        oneQuestion.setAnswer(Arrays.asList("1 p.m."));
        oneQuestionSet.add(oneQuestion);
        questions.setQuestionDTOS(oneQuestionSet);
        expected.add(questions);

        AtomicInteger i= new AtomicInteger();
        List<Arguments> list = new ArrayList<>();
        Set<Contact> contacts = survey.getContacts();
        contacts.forEach(contact ->
                list.add(Arguments.of(contact, survey
                        , expected.get(i.getAndIncrement())))
        );

        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("sourceToSetOneQuestionDTO")
    void toSetOneQuestionDTO(List<SurveyQuestion> surveyQuestions, Contact contact,
                             Set<OneQuestionSeparatelyStatisticDTO> expected) {
        Set<OneQuestionSeparatelyStatisticDTO> dtoSet =
                separatelyStatisticMapper.toSetOneQuestionDTO(surveyQuestions,contact);
        assertArrayEquals(expected.toArray(),dtoSet.toArray());

    }

    static Stream<Arguments> sourceToSetOneQuestionDTO(){
        List<Set<OneQuestionSeparatelyStatisticDTO>> expected
                = new ArrayList<>();
        Set<OneQuestionSeparatelyStatisticDTO> dtoList = new HashSet<>();
        OneQuestionSeparatelyStatisticDTO dto = new OneQuestionSeparatelyStatisticDTO();
        dto.setIndex(1);
        dto.setType(SurveyQuestionType.TEXTAREA);
        dto.setQuestion("What's your name?");
        dto.setChoiceAnswers(new String[0]);
        dto.setAnswer(Arrays.asList("Vlad"));
        dtoList.add(dto);

        dto = new OneQuestionSeparatelyStatisticDTO();
        dto.setIndex(2);
        dto.setType(SurveyQuestionType.RADIOBUTTON);
        dto.setQuestion("What's time?");
        dto.setChoiceAnswers(new String[]{"1 a.m.","1 p.m.","3 p.m."});
        dto.setAnswer(Arrays.asList("1 p.m."));
        dtoList.add(dto);
        expected.add(dtoList);

        AtomicInteger i= new AtomicInteger();
        List<Arguments> list = new ArrayList<>();
        List<SurveyQuestion> surveyQuestions = survey.getSurveyQuestions();
        Set<Contact> contacts = survey.getContacts();
        contacts.forEach(contact ->
                    list.add(Arguments.of(surveyQuestions, contact
                            , expected.get(i.getAndIncrement())))
        );

        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("sourceToOneQuestionDTO")
    void toOneQuestionDTO(SurveyQuestion surveyQuestion, Contact contact,
                          OneQuestionSeparatelyStatisticDTO expected) throws JsonProcessingException {

        OneQuestionSeparatelyStatisticDTO dto =
                separatelyStatisticMapper.toOneQuestionDTO(surveyQuestion,contact);
        assertEquals(expected,dto);
    }

    static Stream<Arguments> sourceToOneQuestionDTO(){
        List<OneQuestionSeparatelyStatisticDTO> expected = new ArrayList<>();
        OneQuestionSeparatelyStatisticDTO dto = new OneQuestionSeparatelyStatisticDTO();
        dto.setIndex(1);
        dto.setType(SurveyQuestionType.TEXTAREA);
        dto.setQuestion("What's your name?");
        dto.setChoiceAnswers(new String[0]);
        dto.setAnswer(Arrays.asList("Vlad"));
        expected.add(dto);

        dto = new OneQuestionSeparatelyStatisticDTO();
        dto.setIndex(2);
        dto.setType(SurveyQuestionType.RADIOBUTTON);
        dto.setQuestion("What's time?");
        dto.setChoiceAnswers(new String[]{"1 a.m.","1 p.m.","3 p.m."});
        dto.setAnswer(Arrays.asList("1 p.m."));
        expected.add(dto);

        AtomicInteger i = new AtomicInteger();
        List<Arguments> list = new ArrayList<>();
        List<SurveyQuestion> surveyQuestions = survey.getSurveyQuestions();
        Set<Contact> contacts = survey.getContacts();
        surveyQuestions.forEach(surveyQuestion -> {
            contacts.forEach(contact ->
                    list.add(Arguments.of(surveyQuestion, contact,expected.get(i.getAndIncrement()))));
        });
        return list.stream();
    }

    @ParameterizedTest
    @MethodSource("sourceTransformationToAnswer")
    void transformationToAnswer(SurveyQuestion surveyQuestion, Contact contact,List<String> expected) {
        List<String> answers =
                separatelyStatisticMapper.transformationToAnswer(surveyQuestion,contact);
        assertFalse(answers.isEmpty());
        assertArrayEquals(expected.toArray(),answers.toArray());
    }

    static Stream<Arguments> sourceTransformationToAnswer(){
        List<List<String>> expected = new ArrayList<>();
        expected.add(Arrays.asList("Vlad"));
        expected.add(Arrays.asList("1 p.m."));

        AtomicInteger i= new AtomicInteger();
        List<Arguments> list = new ArrayList<>();
        List<SurveyQuestion> surveyQuestions = survey.getSurveyQuestions();
        Set<Contact> contacts = survey.getContacts();
        surveyQuestions.forEach(surveyQuestion -> {
            contacts.forEach(contact ->
                    list.add(Arguments.of(surveyQuestion, contact,expected.get(i.getAndIncrement()))));
        });
        return list.stream();
    }

}
