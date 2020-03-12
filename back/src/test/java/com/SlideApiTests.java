package com;

import com.google.gson.Gson;
import com.slide.Slide;
import com.slide.SlideService;
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
public class SlideApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private SlideService slideService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Slide slide = new Slide();
        slide.setId(1);
        slide.setContent("Test Content");

        ArrayList<Slide> slides = new ArrayList<>();
        slides.add(slide);

        given(slideService.findAll()).willReturn(slides);
        given(slideService.findOne(1)).willReturn(Optional.of(slide));
        given(slideService.findOne(2)).willReturn(Optional.empty());
    }

    @Test
    public void testGetSlides() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/slides/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content", is("Test Content")));
    }

    @Test
    public void testGetSlide() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/slides/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Test Content")));
    }

    @Test
    public void testNotFoundGetSlide() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/slides/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteSlide() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/slides/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Test Content")));
    }

    @Test
    public void testNotFoundDeleteSlide() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/slides/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testUpdateSlide() throws Exception{
        Slide slide1 = new Slide();
        slide1.setContent("Test New Content");

        mvc.perform(MockMvcRequestBuilders.put("/api/slides/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(slide1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("Test New Content")));
    }

    @Test
    public void testNotFoundUpdateSlide() throws Exception{
        Slide slide1 = new Slide();
        slide1.setContent("Test New Content");

        mvc.perform(MockMvcRequestBuilders.put("/api/slides/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(slide1)))
                .andExpect(status().is(404));
    }
}
