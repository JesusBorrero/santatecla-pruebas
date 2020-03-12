package com;

import com.google.gson.Gson;
import com.itinerary.lesson.Lesson;
import com.itinerary.lesson.LessonService;
import com.slide.Slide;
import com.slide.SlideService;
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
public class UnitLessonApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private LessonService lessonService;

    @MockBean
    private UnitService unitService;

    @MockBean
    private SlideService slideService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setName("Test Lesson");

        Slide slide = new Slide();
        slide.setId(1);
        slide.setContent("Test Content");

        given(unitService.findOne(1)).willReturn(Optional.of(unit));
        given(unitService.findOne(2)).willReturn(Optional.empty());
        given(lessonService.findOne(1)).willReturn(Optional.of(lesson));
        given(slideService.findOne(1)).willReturn(Optional.of(slide));
    }

    @Test
    public void testGetSlideFromLesson() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/lessons/1/slides/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Test Content")));
    }

    @Test
    public void testNotFoundGetSlideFromLesson() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/lessons/1/slides/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testAddLesson() throws Exception{
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setName("Test Lesson");

        given(unitService.findOne(1)).willReturn(Optional.of(unit));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/1/lessons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(lesson)))
                .andExpect(status().is(201));

        assertThat(unit.getLessons().size(), is(1));
    }

    @Test
    public void testDeleteLesson() throws Exception{
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        Lesson lesson = new Lesson();
        lesson.setId(1);
        lesson.setName("Test Lesson");

        unit.addLesson(lesson);

        given(unitService.findOne(1)).willReturn(Optional.of(unit));
        given(lessonService.findOne(1)).willReturn(Optional.of(lesson));

        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1/lessons/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(unit.getLessons().size(), is(0));
    }

    @Test
    public void testNotFoundDeleteLesson() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/2/lessons/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
