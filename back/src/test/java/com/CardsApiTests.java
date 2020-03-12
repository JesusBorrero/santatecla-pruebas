package com;

import com.card.Card;
import com.card.CardService;
import com.google.gson.Gson;
import com.unit.Unit;
import com.unit.UnitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.junit.Assert.assertThat;
import org.springframework.security.test.context.support.WithMockUser;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class CardsApiTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UnitService unitService;

    @MockBean
    private CardService cardService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Unit unit1 = new Unit();
        Card card1 = new Card();
        card1.setContent("TestContent");
        unit1.addCard(card1);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(unitService.findOne(2)).willReturn(Optional.empty());
        given(cardService.findOne(1)).willReturn(Optional.of(card1));
        given(cardService.findOne(2)).willReturn(Optional.empty());
    }

    @Test
    public void testGetCards() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/cards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content", is("TestContent")));
    }

    @Test
    public void testNotFoundGetCards() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2/cards")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testCreateCard() throws Exception{
        Unit unit1 = new Unit();

        Card card1 = new Card();
        card1.setId(1);
        card1.setContent("TestContent");

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));

        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/cards")
                .content(jsonParser.toJson(card1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        for(Card c : unit1.getCards()){
            assertThat(c.getId(), is(1));
        }
    }

    @Test
    public void testNotFoundCreateCard() throws Exception{
        Card card1 = new Card();
        card1.setId(1);
        card1.setContent("TestContent");

        mvc.perform(MockMvcRequestBuilders.post("/api/units/2/cards")
                .content(jsonParser.toJson(card1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testUploadCard() throws Exception{
        Unit unit1 = new Unit();

        Card card1 = new Card();
        card1.setId(1);
        card1.setContent("TestContent");

        unit1.addCard(card1);

        Card card2 = new Card();
        card2.setId(2);
        card2.setContent("TestContent2");

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(cardService.findOne(1)).willReturn(Optional.of(card1));

        mvc.perform(MockMvcRequestBuilders.put("/api/units/1/cards/1")
                .content(jsonParser.toJson(card2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        for(Card c : unit1.getCards()){
            assertThat(c.getContent(), is("TestContent2"));
        }
    }

    @Test
    public void testNotFoundUploadCard() throws Exception{
        Card card2 = new Card();
        card2.setId(1);
        card2.setContent("TestContent2");

        mvc.perform(MockMvcRequestBuilders.put("/api/units/1/cards/2")
                .content(jsonParser.toJson(card2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testGetCard() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/cards/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", is("TestContent")));
    }

    @Test
    public void testNotFoundGetCard() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/cards/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteCard() throws Exception{
        Unit unit1 = new Unit();

        Card card1 = new Card();
        card1.setId(1);
        card1.setContent("TestContent");

        unit1.addCard(card1);

        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(cardService.findOne(1)).willReturn(Optional.of(card1));

        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1/cards/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(unit1.getCards().iterator().hasNext(), is(Boolean.FALSE));
    }
}
