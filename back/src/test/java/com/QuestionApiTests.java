package com;

import com.question.QuestionService;
import com.question.definition.definition_question.DefinitionQuestion;
import com.question.list.list_question.ListQuestion;
import com.question.test.test_question.TestQuestion;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class QuestionApiTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private UnitService unitService;

    @Before
    public void initialize() {
        Unit unit1 = new Unit();
        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");
        ListQuestion listQuestion = new ListQuestion();
        listQuestion.setQuestionText("Test List Question");
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestionText("Test Test Question");

        unit1.addDefinitionQuestion(definitionQuestion);
        unit1.addTestQuestion(testQuestion);
        unit1.addListQuestion(listQuestion);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(unitService.findOne(2)).willReturn(Optional.empty());
        given(questionService.findOne(1)).willReturn(Optional.of(definitionQuestion));
    }

    @Test
    public void testGetQuestionsFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText", is("Test Definition Question")))
                .andExpect(jsonPath("$[1].questionText", is("Test List Question")));
    }

    @Test
    public void testNotFoundGetQuestionsFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText", is("Test Definition Question")));
    }

    @Test
    public void testNotFoundGetQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1/question/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText", is("Test Definition Question")));
    }

    @Test
    public void testNotFoundDeleteQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/2/question/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetQuestionCorrectCount() throws Exception{
        Unit unit1 = new Unit();

        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");
        definitionQuestion.setTotalCorrectAnswers(1);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(questionService.findOne(1)).willReturn(Optional.of(definitionQuestion));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/1/correctCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString(), is("1"));
    }

    @Test
    public void testNotFoundGetQuestionCorrectCount() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/1/correctCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetQuestionWrongCount() throws Exception{
        Unit unit1 = new Unit();

        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");
        definitionQuestion.setTotalWrongAnswers(1);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(questionService.findOne(1)).willReturn(Optional.of(definitionQuestion));

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/1/wrongCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString(), is("1"));
    }

    @Test
    public void testGetQuestionNotFoundWrongCount() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/1/wrongCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
