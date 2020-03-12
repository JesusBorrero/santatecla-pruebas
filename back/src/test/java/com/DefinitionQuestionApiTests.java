package com;

import com.google.gson.Gson;
import com.question.definition.definition_answer.DefinitionAnswer;
import com.question.definition.definition_question.DefinitionQuestion;
import com.question.definition.definition_question.DefinitionQuestionService;
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
public class DefinitionQuestionApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UnitService unitService;

    @MockBean
    private DefinitionQuestionService definitionQuestionService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Unit unit1 = new Unit();
        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");
        DefinitionQuestion definitionQuestion1 = new DefinitionQuestion();
        definitionQuestion1.setQuestionText("Test Definition Question 2");

        DefinitionAnswer definitionAnswer = new DefinitionAnswer();
        definitionAnswer.setJustification("Test Justification");

        definitionQuestion.addAnswer(definitionAnswer);

        unit1.addDefinitionQuestion(definitionQuestion);
        unit1.addDefinitionQuestion(definitionQuestion1);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(unitService.findOne(2)).willReturn(Optional.empty());
        given(definitionQuestionService.findOne(1)).willReturn(Optional.of(definitionQuestion));
    }

    @Test
    public void testGetDefinitionQuestionsFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/definition")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].questionText", is("Test Definition Question")))
                .andExpect(jsonPath("$[1].questionText", is("Test Definition Question 2")));
    }

    @Test
    public void testNotFoundGetDefinitionQuestionsFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/definition")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetDefinitionQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/definition/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText", is("Test Definition Question")));
    }

    @Test
    public void testNotFoundGetDefinitionQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/definition/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testAddDefinitionQuestion() throws Exception{
        Unit unit1 = new Unit();

        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/1/question/definition/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(definitionQuestion)))
                .andExpect(status().is(201));

        assertThat(unit1.getDefinitionQuestions().size(), is(1));
    }

    @Test
    public void testNotFoundAddDefinitionQuestion() throws Exception{
        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");

        mvc.perform(MockMvcRequestBuilders.post("/api/units/2/question/definition/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(definitionQuestion)))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteDefinitionQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1/question/definition/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundDeleteDefinitionQuestion() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/2/question/definition/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testUpdateDefinitionQuestion() throws Exception{
        DefinitionQuestion definitionQuestion2 = new DefinitionQuestion();
        definitionQuestion2.setQuestionText("Test Definition Question 2");

        mvc.perform(MockMvcRequestBuilders.put("/api/units/1/question/definition/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(definitionQuestion2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionText", is("Test Definition Question 2")));
    }

    @Test
    public void testNotFoundUpdateDefinitionQuestion() throws Exception{
        DefinitionQuestion definitionQuestion2 = new DefinitionQuestion();
        definitionQuestion2.setQuestionText("Test Definition Question 2");

        mvc.perform(MockMvcRequestBuilders.put("/api/units/2/question/definition/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(definitionQuestion2)))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetAllDefinitionQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/definition/1/answer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].justification", is("Test Justification")));
    }

    @Test
    public void testGetCorrectedDefinitionQuestionAnswers() throws Exception{
        Unit unit1 = new Unit();

        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");

        DefinitionAnswer definitionAnswer = new DefinitionAnswer();
        definitionAnswer.setJustification("Test Justification");
        definitionAnswer.setCorrected(true);

        DefinitionAnswer definitionAnswer2 = new DefinitionAnswer();
        definitionAnswer2.setJustification("Test Justification 2");
        definitionAnswer2.setCorrected(false);

        definitionQuestion.addAnswer(definitionAnswer);
        definitionQuestion.addAnswer(definitionAnswer2);

        ArrayList<DefinitionAnswer> answers = new ArrayList<>();
        answers.add(definitionAnswer);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(definitionQuestionService.findOne(1)).willReturn(Optional.of(definitionQuestion));
        given(definitionQuestionService.findCorrectedAnswers(1)).willReturn(Optional.of(answers));

        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/definition/1/answer?corrected=true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].justification", is("Test Justification")));
    }

    @Test
    public void testGetNotCorrectedDefinitionQuestionAnswers() throws Exception{
        Unit unit1 = new Unit();

        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");

        DefinitionAnswer definitionAnswer = new DefinitionAnswer();
        definitionAnswer.setJustification("Test Justification");
        definitionAnswer.setCorrected(true);

        DefinitionAnswer definitionAnswer2 = new DefinitionAnswer();
        definitionAnswer2.setJustification("Test Justification 2");
        definitionAnswer2.setCorrected(false);

        definitionQuestion.addAnswer(definitionAnswer);
        definitionQuestion.addAnswer(definitionAnswer2);

        ArrayList<DefinitionAnswer> answers = new ArrayList<>();
        answers.add(definitionAnswer2);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(definitionQuestionService.findOne(1)).willReturn(Optional.of(definitionQuestion));
        given(definitionQuestionService.findNotCorrectedAnswers(1)).willReturn(Optional.of(answers));

        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/definition/1/answer?corrected=false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].justification", is("Test Justification 2")));
    }

    @Test
    public void testNotFoundGetDefinitionQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/definition/1/answer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testAddDefinitionAnswer() throws Exception{
        Unit unit1 = new Unit();

        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");

        DefinitionAnswer definitionAnswer = new DefinitionAnswer();

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(definitionQuestionService.findOne(1)).willReturn(Optional.of(definitionQuestion));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/1/question/definition/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(definitionAnswer)))
                .andExpect(status().is(201));

        assertThat(definitionQuestion.getAnswers().size(), is(1));
    }

    @Test
    public void testNotFoundAddDefinitionAnswer() throws Exception{
        DefinitionAnswer definitionAnswer = new DefinitionAnswer();

        mvc.perform(MockMvcRequestBuilders.post("/api/units/2/question/definition/1/answer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(definitionAnswer)))
                .andExpect(status().is(404));
    }

    @Test
    public void testUpdateDefinitionAnswer() throws Exception{
        Unit unit1 = new Unit();

        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");

        DefinitionAnswer definitionAnswer = new DefinitionAnswer();
        definitionAnswer.setCorrected(false);

        definitionQuestion.addAnswer(definitionAnswer);

        DefinitionAnswer definitionAnswer2 = new DefinitionAnswer();
        definitionAnswer2.setCorrected(true);
        definitionAnswer2.setCorrect(true);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(definitionQuestionService.findOne(1)).willReturn(Optional.of(definitionQuestion));
        given(definitionQuestionService.findOneAnswer(definitionQuestion, 1)).willReturn(Optional.of(definitionAnswer));

        mvc.perform(MockMvcRequestBuilders.put("/api/units/1/question/definition/1/answer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(definitionAnswer2)))
                .andExpect(status().isOk());

        assertThat(definitionQuestion.getTotalCorrectAnswers(), is(1));
    }

    @Test
    public void testNotFoundUpdateDefinitionAnswer() throws Exception{
        DefinitionAnswer definitionAnswer = new DefinitionAnswer();
        definitionAnswer.setCorrected(false);

        mvc.perform(MockMvcRequestBuilders.put("/api/units/2/question/definition/1/answer/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(definitionAnswer)))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetUncorrectedDefinitionQuestionAnswers() throws Exception{
        Unit unit1 = new Unit();

        DefinitionQuestion definitionQuestion = new DefinitionQuestion();
        definitionQuestion.setQuestionText("Test Definition Question");

        DefinitionAnswer definitionAnswer = new DefinitionAnswer();
        definitionAnswer.setCorrected(true);

        DefinitionAnswer definitionAnswer2 = new DefinitionAnswer();

        definitionQuestion.addAnswer(definitionAnswer);
        definitionQuestion.addAnswer(definitionAnswer2);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(definitionQuestionService.findOne(1)).willReturn(Optional.of(definitionQuestion));
        given(definitionQuestionService.findNotCorrectedAnswersCount(1)).willReturn(1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/api/units/1/question/definition/1/uncorrectedCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString(), is("1"));
    }

    @Test
    public void testNotFoundGetUncorrectedDefinitionQuestionAnswers() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/question/definition/1/uncorrectedCount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
