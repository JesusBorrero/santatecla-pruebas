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
import java.util.stream.Collectors;

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

        ArrayList<Unit> units = new ArrayList<>();

        Unit unit1 = new Unit("One");
        unit1.setId(1);
        units.add(unit1);
        Unit unit2 = new Unit("Two");
        unit2.setId(2);
        units.add(unit2);
        Unit unit3 = new Unit("Three");
        unit3.setId(3);
        units.add(unit3);
        Unit unit4 = new Unit("Three");
        unit4.setId(4);
        units.add(unit4);

        Relation relation1 = new Relation();
        relation1.setIncoming((long)1);
        relation1.setOutgoing((long)3);
        unit1.addIncomingRelation(relation1);
        unit2.addOutgoingRelation(relation1);

        Relation relation2 = new Relation();
        relation2.setIncoming((long)2);
        relation2.setOutgoing((long)4);
        unit3.addIncomingRelation(relation2);
        unit4.addOutgoingRelation(relation2);

        given(unitService.findAll()).willReturn(units);
        given(unitService.findOne(1)).willReturn(Optional.of(unit1));
        given(unitService.findOne(2)).willReturn(Optional.of(unit2));
        given(unitService.findOne(3)).willReturn(Optional.of(unit3));
        given(unitService.findOne(4)).willReturn(Optional.of(unit4));

        given(unitService.isValidName(ArgumentMatchers.any())).willReturn(true);

        given(unitService.ableToDeleteUnit(unit1)).willReturn(false);
        given(unitService.ableToDeleteUnit(unit2)).willReturn(false);
        given(unitService.ableToDeleteUnit(unit3)).willReturn(true);
        given(unitService.ableToDeleteUnit(unit4)).willReturn(true);

        given(unitService.findByNameContaining("")).willReturn(units);
        given(unitService.findByNameContaining("w")).willReturn(units.stream().filter(unit -> unit.getName().contains("w")).collect(Collectors.toList()));

        given(unitService.getAbsoluteName(unit1)).willReturn("/One");
        given(unitService.getAbsoluteName(unit2)).willReturn("/Two");
        given(unitService.getAbsoluteName(unit3)).willReturn("One/Three");
        given(unitService.getAbsoluteName(unit4)).willReturn("Two/Three");

        given(relationService.findOne(1)).willReturn(Optional.of(relation1));
        given(relationService.findOne(2)).willReturn(Optional.of(relation2));
        given(relationService.findOne(3)).willReturn(Optional.empty());

    }

    @Test
    public void testGetUnits() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/units/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("One")))
                .andExpect(jsonPath("$[1].name", is("Two")))
                .andExpect(jsonPath("$[2].name", is("Three")))
                .andExpect(jsonPath("$[3].name", is("Three")));
    }

    @Test
    public void testGetUnit() throws Exception {
        int[] ids = {1, 2, 3, 4};
        for (int id : ids) {
            mvc.perform(MockMvcRequestBuilders.get("/api/units/" + id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id", is(id)));
        }
    }

    @Test
    public void testNotFoundGetUnit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/units/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testCreateSimpleUnit() throws Exception {
        String newUnitName1 = "NewUnitOne";
        Unit newUnit1 = new Unit(newUnitName1);
        mvc.perform(MockMvcRequestBuilders.post("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(newUnit1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newUnitName1)));
        String newUnitName2 = "NewUnitTwo";
        Unit newUnit2 = new Unit(newUnitName2);
        mvc.perform(MockMvcRequestBuilders.post("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(newUnit2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newUnitName2)));
    }

    @Test
    public void testCreateRelation() throws Exception {
        Unit incoming = new Unit("Incoming");
        Unit outgoing = new Unit("Outgoing");

        Relation relation = new Relation();
        relation.setIncoming((long)1);
        relation.setOutgoing((long)2);
        incoming.addIncomingRelation(relation);
        outgoing.addOutgoingRelation(relation);

        mvc.perform(MockMvcRequestBuilders.post("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(incoming)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Incoming")))
                .andExpect(jsonPath("$.incomingRelations[0].incoming", is(1)));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(outgoing)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Outgoing")))
                .andExpect(jsonPath("$.outgoingRelations[0].outgoing", is(2)));
    }

    @Test
    public void testConflictCreateUnit() throws Exception {
        Unit unit = new Unit("ConflictUnitName");

        given(unitService.isValidName(ArgumentMatchers.any())).willReturn(false);

        mvc.perform(MockMvcRequestBuilders.post("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(unit)))
                .andExpect(status().is(409));
    }

    @Test
    public void testUpdateUnits() throws Exception {
        Unit unit = new Unit("UpdateUnitOne");
        unit.setId(1);
        Unit unit2 = new Unit("UpdateUnitTwo");
        unit2.setId(2);

        ArrayList<Unit> units = new ArrayList<>();
        units.add(unit);
        units.add(unit2);

        mvc.perform(MockMvcRequestBuilders.put("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(units)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("UpdateUnitOne")))
                .andExpect(jsonPath("$[1].name", is("UpdateUnitTwo")));
    }

    @Test
    public void testNotFoundUpdateUnits() throws Exception {
        Unit unit = new Unit("UpdateUnitOne");
        unit.setId(1);
        Unit unit2 = new Unit("UpdateUnitTen");
        unit2.setId(10);

        ArrayList<Unit> units = new ArrayList<>();
        units.add(unit);
        units.add(unit2);

        mvc.perform(MockMvcRequestBuilders.put("/api/units/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(units)))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteUnit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testConflictDeleteUnit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(409));
    }

    @Test
    public void testNotFoundUnit() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testSearchUnits() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/units/search?name=")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("One")))
                .andExpect(jsonPath("$[1].name", is("Two")))
                .andExpect(jsonPath("$[2].name", is("Three")))
                .andExpect(jsonPath("$[3].name", is("Three")));
        mvc.perform(MockMvcRequestBuilders.get("/api/units/search?name=w")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Two")));
    }

    @Test
    public void testGetAbsoluteName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/absoluteName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("/One")));
        mvc.perform(MockMvcRequestBuilders.get("/api/units/3/absoluteName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("One/Three")));
        mvc.perform(MockMvcRequestBuilders.get("/api/units/4/absoluteName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Two/Three")));
    }

    @Test
    public void testValidName() throws Exception {
        Unit unit = new Unit("ValidName");
        unit.setId(1);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/units/valid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(unit)))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString(), is("true"));
    }

    @Test
    public void testGetName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/units/1/name")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response", is("One")));
    }

}
