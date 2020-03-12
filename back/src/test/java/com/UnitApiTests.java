package com;

import com.google.gson.Gson;
import com.relation.Relation;
import com.relation.RelationService;
import com.unit.Unit;
import com.unit.UnitService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
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
public class UnitApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UnitService unitService;

    @MockBean
    private RelationService relationService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        ArrayList<Unit> units = new ArrayList<>();
        units.add(unit);

        Relation relation = new Relation();
        relation.setIncoming((long)4);
        relation.setOutgoing((long)1);

        given(unitService.findAll()).willReturn(units);
        given(unitService.findOne(1)).willReturn(Optional.of(unit));
        given(unitService.findOne(2)).willReturn(Optional.empty());
        given(unitService.findOne(3)).willReturn(Optional.of(unit));
        given(unitService.isValidName(ArgumentMatchers.any())).willReturn(true);
        given(unitService.getAbsoluteName(ArgumentMatchers.any())).willReturn("/Test Unit");
        given(unitService.findByNameContaining("Te")).willReturn(units);
        given(relationService.findOne(1)).willReturn(Optional.of(relation));
        given(relationService.findOne(2)).willReturn(Optional.empty());
    }

    @Test
    public void testGetUnits() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Unit")));
    }

    @Test
    public void testGetUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Unit")));
    }

    @Test
    public void testNotFoundGetUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testCreateSimpleUnit() throws Exception{
        Unit unit = new Unit();
        unit.setName("Test Unit");

        mvc.perform(MockMvcRequestBuilders.post("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(unit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Unit")));
    }

    @Test
    public void testCreateIncomingAndOutgoingRelationsUnit() throws Exception{
        Unit unit = new Unit();
        unit.setName("Test Unit");

        Relation relation = new Relation();
        relation.setIncoming((long)1);
        relation.setOutgoing((long)2);
        unit.addOutgoingRelation(relation);

        Relation relation1 = new Relation();
        relation1.setIncoming((long)3);
        relation1.setOutgoing((long)1);
        unit.addIncomingRelation(relation1);

        mvc.perform(MockMvcRequestBuilders.post("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(unit)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Unit")))
                .andExpect(jsonPath("$.incomingRelations[0].incoming", is(3)));
    }

    @Test
    public void testConflictCreateUnit() throws Exception{
        Unit unit = new Unit();
        unit.setName("Test Unit");

        given(unitService.isValidName(ArgumentMatchers.any())).willReturn(false);

        mvc.perform(MockMvcRequestBuilders.post("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(unit)))
                .andExpect(status().is(409));
    }

    @Test
    public void testUpdateUnits() throws Exception{
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        Unit unit2 = new Unit();
        unit2.setId(3);
        unit2.setName("Test Unit 2");

        ArrayList<Unit> units = new ArrayList<>();
        units.add(unit);
        units.add(unit2);

        mvc.perform(MockMvcRequestBuilders.put("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(units)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].name", is("Test Unit 2")));
    }

    @Test
    public void testNotFoundUpdateUnits() throws Exception{
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        Unit unit2 = new Unit();
        unit2.setId(2);
        unit2.setName("Test Unit 2");

        ArrayList<Unit> units = new ArrayList<>();
        units.add(unit);
        units.add(unit2);

        mvc.perform(MockMvcRequestBuilders.put("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(units)))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteRelation() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/relations/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testNotFoundDeleteRelation() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/relations/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testSearchUnits() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/search?name=Te")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Unit")));
    }

    @Test
    public void testGetAbsoluteName() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/absoluteName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("/Test Unit")));
    }

    @Test
    public void testValidName() throws Exception{
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/units/valid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(unit)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString(), is("true"));
    }

    @Test
    public void testGetName() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is("Test Unit")));
    }
}
