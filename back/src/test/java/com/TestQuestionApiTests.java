package com;

import com.google.gson.Gson;
import com.question.test.test_answer.TestAnswer;
import com.question.test.test_question.TestQuestion;
import com.question.test.test_question.TestQuestionService;
import com.unit.Unit;
import com.unit.UnitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class TestQuestionApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UnitService unitService;

    @MockBean
    private TestQuestionService testQuestionService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Unit unit1 = new Unit();
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestionText("Test Test Question");
        TestQuestion testQuestion1 = new TestQuestion();
        testQuestion1.setQuestionText("Test Test Question 2");

        unit1.addTestQuestion(testQuestion);
        unit1.addTestQuestion(testQuestion1);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(unitService.findOne(2)).willReturn(Optional.empty());
        given(testQuestionService.findOne(1)).willReturn(Optional.of(testQuestion));
    }

    @Test
    public void testGetTestQuestionsFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText", is("Test Test Question")))
                .andExpect(jsonPath("$[1].questionText", is("Test Test Question 2")));
    }

    @Test
    public void testNotFoundGetTestQuestionsFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetTestQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/test/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText", is("Test Test Question")));
    }

    @Test
    public void testNotFoundGetTestQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/test/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testAddTestQuestion() throws Exception{
        Unit unit1 = new Unit();

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestionText("Test Test Question");

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/1/question/test/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(testQuestion)))
                .andExpect(status().is(201));

        assertThat(unit1.getTestQuestions().size(), is(1));
    }

    @Test
    public void testNotFoundAddTestQuestion() throws Exception{
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestionText("Test Test Question");

        mvc.perform(MockMvcRequestBuilders.post("/api/units/2/question/test/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(testQuestion)))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteTestQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1/question/test/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundDeleteTestQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/2/question/test/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testUpdateTestQuestion() throws Exception{
        Unit unit1 = new Unit();

        ArrayList<String> possibleAnswers = new ArrayList<>();
        possibleAnswers.add("Possible Answer");
        TestQuestion testQuestion = new TestQuestion("Test Test Question", possibleAnswers,"Correct Answer");

        unit1.addTestQuestion(testQuestion);

        TestQuestion testQuestion1 = new TestQuestion("Test Test Question 2", possibleAnswers,"New Correct Answer");

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(testQuestionService.findOne(1)).willReturn(Optional.of(testQuestion));

        mvc.perform(MockMvcRequestBuilders.put("/api/units/1/question/test/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(testQuestion1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText", is("Test Test Question 2")));
    }

    @Test
    public void testNotFoundUpdateTestQuestion() throws Exception{
        TestQuestion testQuestion2 = new TestQuestion();
        testQuestion2.setQuestionText("Test Test Question 2");

        mvc.perform(MockMvcRequestBuilders.put("/api/units/2/question/test/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(testQuestion2)))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetTestQuestionAnswers() throws Exception{
        Unit unit1 = new Unit();

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestionText("Test Test Question");

        TestAnswer testAnswer = new TestAnswer();
        testAnswer.setCorrect(true);

        testQuestion.addAnswer(testAnswer);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(testQuestionService.findOne(1)).willReturn(Optional.of(testQuestion));

        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/test/1/answer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correct", is(true)));
    }

    @Test
    public void testNotFoundGetTestQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/test/1/answer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testAddTestAnswer() throws Exception{
        Unit unit1 = new Unit();

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestionText("Test Test Question");

        TestAnswer testAnswer = new TestAnswer();

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(testQuestionService.findOne(1)).willReturn(Optional.of(testQuestion));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/1/question/test/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(testAnswer)))
                .andExpect(status().is(201));

        assertThat(testQuestion.getTestAnswers().size(), is(1));
    }

    @Test
    public void testNotFoundAddTestAnswer() throws Exception{
        TestAnswer testAnswer = new TestAnswer();

        mvc.perform(MockMvcRequestBuilders.post("/api/units/2/question/test/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(testAnswer)))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetChosenWrongTestQuestionAnswers() throws Exception{
        Unit unit1 = new Unit();

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestionText("Test Test Question");

        TestAnswer testAnswer = new TestAnswer();

        testQuestion.addAnswer(testAnswer);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(testQuestionService.findOne(1)).willReturn(Optional.of(testQuestion));
        given(testQuestionService.findChosenWrongAnswersCount(1)).willReturn(new ArrayList<>());

        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/test/1/chosenWrongAnswersCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testNotFoundGetChosenWrongTestQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/test/1/chosenWrongAnswersCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
