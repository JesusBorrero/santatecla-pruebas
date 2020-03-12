package com;

import com.google.gson.Gson;
import com.question.list.list_answer.ListAnswer;
import com.question.list.list_question.ListQuestion;
import com.question.list.list_question.ListQuestionService;
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
public class ListQuestionApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UnitService unitService;

    @MockBean
    private ListQuestionService listQuestionService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Unit unit1 = new Unit();
        ListQuestion listQuestion = new ListQuestion();
        listQuestion.setQuestionText("Test List Question");
        ListQuestion listQuestion1 = new ListQuestion();
        listQuestion1.setQuestionText("Test List Question 2");

        ListAnswer listAnswer = new ListAnswer();
        listAnswer.setCorrect(true);

        listQuestion.addAnswer(listAnswer);

        unit1.addListQuestion(listQuestion);
        unit1.addListQuestion(listQuestion1);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(unitService.findOne(2)).willReturn(Optional.empty());
        given(listQuestionService.findOne(1)).willReturn(Optional.of(listQuestion));
        given(listQuestionService.findChosenWrongAnswersCount(1)).willReturn(new ArrayList<>());
    }

    @Test
    public void testGetListQuestionsFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText", is("Test List Question")))
                .andExpect(jsonPath("$[1].questionText", is("Test List Question 2")));
    }

    @Test
    public void testNotFoundGetListQuestionsFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetListQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/list/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText", is("Test List Question")));
    }

    @Test
    public void testNotFoundGetListQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/list/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testAddListQuestion() throws Exception{
        Unit unit1 = new Unit();

        ListQuestion listQuestion = new ListQuestion();
        listQuestion.setQuestionText("Test List Question");

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/1/question/list/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(listQuestion)))
                .andExpect(status().is(201));

        assertThat(unit1.getListQuestions().size(), is(1));
    }

    @Test
    public void testNotFoundAddListQuestion() throws Exception{
        ListQuestion listQuestion = new ListQuestion();
        listQuestion.setQuestionText("Test List Question");

        mvc.perform(MockMvcRequestBuilders.post("/api/units/2/question/list/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(listQuestion)))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteListQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1/question/list/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundDeleteListQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/2/question/list/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testUpdateListQuestion() throws Exception{
        ListQuestion listQuestion1 = new ListQuestion();
        listQuestion1.setQuestionText("Test List Question 2");

        mvc.perform(MockMvcRequestBuilders.put("/api/units/1/question/list/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(listQuestion1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText", is("Test List Question 2")));
    }

    @Test
    public void testNotFoundUpdateListQuestion() throws Exception{
        ListQuestion listQuestion2 = new ListQuestion();
        listQuestion2.setQuestionText("Test List Question 2");

        mvc.perform(MockMvcRequestBuilders.put("/api/units/2/question/list/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(listQuestion2)))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetListQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/list/1/answer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correct", is(true)));
    }

    @Test
    public void testNotFoundGetListQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/list/1/answer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testAddListAnswer() throws Exception{
        Unit unit1 = new Unit();

        ListQuestion listQuestion = new ListQuestion();
        listQuestion.setQuestionText("Test List Question");

        ListAnswer listAnswer = new ListAnswer();

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(listQuestionService.findOne(1)).willReturn(Optional.of(listQuestion));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/1/question/list/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(listAnswer)))
                .andExpect(status().is(201));

        assertThat(listQuestion.getListAnswers().size(), is(1));
    }

    @Test
    public void testNotFoundAddListAnswer() throws Exception{
        ListAnswer listAnswer = new ListAnswer();

        mvc.perform(MockMvcRequestBuilders.post("/api/units/2/question/list/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(listAnswer)))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetChosenWrongListQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/list/1/chosenWrongAnswersCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testNotFoundGetChosenWrongListQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/list/1/chosenWrongAnswersCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
