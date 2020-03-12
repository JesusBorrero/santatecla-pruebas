package com;

import com.google.gson.Gson;
import com.itinerary.lesson.Lesson;
import com.itinerary.lesson.LessonService;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class LessonApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private LessonService lessonService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Lesson lesson = new Lesson();
        lesson.setName("Test Lesson");
        ArrayList<Lesson> lessons = new ArrayList<>();
        lessons.add(lesson);

        given(lessonService.findAll()).willReturn(lessons);
        given(lessonService.findOne(1)).willReturn(Optional.of(lesson));
        given(lessonService.findOne(2)).willReturn(Optional.empty());
    }

    @Test
    public void testGetLessons() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/lessons/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Lesson")));
    }

    @Test
    public void testGetLesson() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/lessons/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Lesson")));
    }

    @Test
    public void testNotFoundGetLesson() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/lessons/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteLesson() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/lessons/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundDeleteLesson() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/lessons/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testEditLesson() throws Exception{
        Lesson lesson2 = new Lesson();
        lesson2.setName("Test Lesson 2");

        mvc.perform(MockMvcRequestBuilders.put("/api/lessons/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(lesson2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Lesson 2")));
    }
}
