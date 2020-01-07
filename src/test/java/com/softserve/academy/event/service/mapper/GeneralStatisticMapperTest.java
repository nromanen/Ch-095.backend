package com.softserve.academy.event.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softserve.academy.event.dto.OneQuestionGeneralStatisticDTO;
import com.softserve.academy.event.dto.QuestionsGeneralStatisticDTO;
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


class GeneralStatisticMapperTest {


    private GeneralStatisticMapper generalStatisticMapper;
    private static Survey survey;

    @BeforeAll
    static void initializeDTO(){
        survey = new Survey();
        survey.setTitle("Survey #1");
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
        generalStatisticMapper = Mappers.getMapper(GeneralStatisticMapper.class);
    }

    @ParameterizedTest
    @MethodSource("sourceToQuestionsDTO")
    void toQuestionsDTO(Survey survey,QuestionsGeneralStatisticDTO expected) {
        QuestionsGeneralStatisticDTO actual =
                generalStatisticMapper.toQuestionsDTO(survey);
        assertEquals(actual,expected);
    }

    @Test
    void toQuestionsDTOWithoutQuestions(){
        Survey survey = new Survey();
        survey.setTitle("Survey #1");
        QuestionsGeneralStatisticDTO dto =
                generalStatisticMapper.toQuestionsDTO(survey);
        assertTrue(dto.getQuestionDTOS().isEmpty());
    }

    @Test
    void toQuestionsDTOWithoutAnswers(){
        Survey survey = new Survey();
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
        assertDoesNotThrow(() -> generalStatisticMapper.toQuestionsDTO(survey));
    }

    @Test
    void ifChoiceAnswersIsNotCorrect(){
        Survey survey = new Survey();
        survey.setTitle("Survey #1");
        List<SurveyQuestion> surveyQuestions = new ArrayList<>();
        SurveyQuestion question = new SurveyQuestion();
        question.setType(SurveyQuestionType.TEXTAREA);
        question.setChoiceAnswers("adsdsa");
        surveyQuestions.add(question);
        survey.setSurveyQuestions(surveyQuestions);
        assertThrows(RuntimeException.class,() -> generalStatisticMapper.toQuestionsDTO(survey));
    }

    static Stream<Arguments> sourceToQuestionsDTO(){
        QuestionsGeneralStatisticDTO expected =
                new QuestionsGeneralStatisticDTO();
        expected.setTitle("Survey #1");
        Set<OneQuestionGeneralStatisticDTO> oneQuestionsSet = new HashSet<>();
        OneQuestionGeneralStatisticDTO oneQuestion = new OneQuestionGeneralStatisticDTO();
        oneQuestion.setIndex(1);
        oneQuestion.setChoiceAnswers(new String[0]);
        oneQuestion.setType(SurveyQuestionType.TEXTAREA);
        oneQuestion.setQuestion("What's your name?");
        List<List<String >> answers = new ArrayList<>();
        answers.add(Arrays.asList("Vlad"));
        oneQuestion.setAnswers(answers);
        oneQuestionsSet.add(oneQuestion);

        oneQuestion = new OneQuestionGeneralStatisticDTO();
        oneQuestion.setIndex(2);
        oneQuestion.setChoiceAnswers(new String[]{"1 a.m.","1 p.m.","3 p.m."});
        oneQuestion.setType(SurveyQuestionType.RADIOBUTTON);
        oneQuestion.setQuestion("What's time?");
        answers = new ArrayList<>();
        answers.add(Arrays.asList("1 p.m."));
        oneQuestion.setAnswers(answers);
        oneQuestionsSet.add(oneQuestion);

        expected.setQuestionDTOS(oneQuestionsSet);

        return Stream.of(Arguments.of(survey,expected));
    }
    @ParameterizedTest
    @MethodSource("sourceOneQuestion")
    void toOneQuestionDTO(SurveyQuestion surveyQuestion, OneQuestionGeneralStatisticDTO expected)
            throws JsonProcessingException {

        OneQuestionGeneralStatisticDTO actual =
                generalStatisticMapper.toOneQuestionDTO(surveyQuestion);
        assertEquals(expected,actual);
    }

    static Stream<Arguments> sourceOneQuestion(){
        List<OneQuestionGeneralStatisticDTO> expected = new ArrayList<>();
        OneQuestionGeneralStatisticDTO dto = new OneQuestionGeneralStatisticDTO();
        dto.setIndex(1);
        dto.setChoiceAnswers(new String[0]);
        dto.setType(SurveyQuestionType.TEXTAREA);
        dto.setQuestion("What's your name?");
        List<List<String >> answers = new ArrayList<>();
        answers.add(Arrays.asList("Vlad"));
        dto.setAnswers(answers);
        expected.add(dto);

        dto = new OneQuestionGeneralStatisticDTO();
        dto.setIndex(2);
        dto.setChoiceAnswers(new String[]{"1 a.m.","1 p.m.","3 p.m."});
        dto.setType(SurveyQuestionType.RADIOBUTTON);
        dto.setQuestion("What's time?");
        answers = new ArrayList<>();
        answers.add(Arrays.asList("1 p.m."));
        dto.setAnswers(answers);
        expected.add(dto);

        List<Arguments> arguments = new ArrayList<>();
        AtomicInteger i= new AtomicInteger();
        survey.getSurveyQuestions().forEach(value ->{
            arguments.add(Arguments.of(value,expected.get(i.getAndIncrement())));
        });

        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("sourceListOneQuestionToDTO")
    void listOneQuestionToDTO(List<SurveyQuestion> surveyQuestions,
                              List<OneQuestionGeneralStatisticDTO> expected) {
        List<OneQuestionGeneralStatisticDTO> actual =
                generalStatisticMapper.listOneQuestionToDTO(surveyQuestions);

        assertArrayEquals(expected.toArray(),actual.toArray());
    }

    static Stream<Arguments> sourceListOneQuestionToDTO() {
        List<OneQuestionGeneralStatisticDTO> expected = new ArrayList<>();
        OneQuestionGeneralStatisticDTO dto = new OneQuestionGeneralStatisticDTO();
        dto.setIndex(1);
        dto.setChoiceAnswers(new String[0]);
        dto.setType(SurveyQuestionType.TEXTAREA);
        dto.setQuestion("What's your name?");
        List<List<String >> answers = new ArrayList<>();
        answers.add(Arrays.asList("Vlad"));
        dto.setAnswers(answers);
        expected.add(dto);

        dto = new OneQuestionGeneralStatisticDTO();
        dto.setIndex(2);
        dto.setChoiceAnswers(new String[]{"1 a.m.","1 p.m.","3 p.m."});
        dto.setType(SurveyQuestionType.RADIOBUTTON);
        dto.setQuestion("What's time?");
        answers = new ArrayList<>();
        answers.add(Arrays.asList("1 p.m."));
        dto.setAnswers(answers);
        expected.add(dto);

        return Stream.of(Arguments.of(survey.getSurveyQuestions(),expected));
    }

    @ParameterizedTest
    @MethodSource("sourceTransformationToAnswers")
    void transformationToAnswers(SurveyQuestion surveyQuestion,
                                 List<List<String>> expected) {
        List<List<String>> answers =
                generalStatisticMapper.transformationToAnswers(surveyQuestion);
        assertFalse(answers.isEmpty());
        assertArrayEquals(expected.toArray(),answers.toArray());

    }

    static Stream<Arguments> sourceTransformationToAnswers(){
        List<List<List<String>>> expected = new ArrayList<>();
        List<List<String >> answers = new ArrayList<>();
        answers.add(Arrays.asList("Vlad"));
        expected.add(answers);

        answers = new ArrayList<>();
        answers.add(Arrays.asList("1 p.m."));
        expected.add(answers);

        List<Arguments> list = new ArrayList<>();
        List<SurveyQuestion> questions = survey.getSurveyQuestions();
        AtomicInteger i= new AtomicInteger();

        questions.forEach(surveyQuestion -> {
            list.add(Arguments.of(surveyQuestion,expected.get(i.getAndIncrement())));
        });

        return list.stream();
    }
}





























