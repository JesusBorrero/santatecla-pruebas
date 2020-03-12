package com;

import com.google.gson.Gson;
import com.itinerary.block.BlockService;
import com.itinerary.module.Module;
import com.itinerary.module.ModuleService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles="ADMIN")
public class UnitModuleApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UnitService unitService;

    @MockBean
    private ModuleService moduleService;

    @MockBean
    private BlockService blockService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        given(unitService.findOne(2)).willReturn(Optional.empty());
    }

    @Test
    public void testAddModuleToUnit() throws Exception{
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        Module module = new Module();
        module.setName("Test Module");

        given(unitService.findOne(1)).willReturn(Optional.of(unit));

        mvc.perform(MockMvcRequestBuilders.post("/api/units/1/modules")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(module)))
                .andExpect(status().is(201));

        assertThat(unit.getModules().size(), is(1));
    }

    @Test
    public void testDeleteModuleFromUnit() throws Exception{
        Unit unit = new Unit();
        unit.setId(1);
        unit.setName("Test Unit");

        Module module = new Module();
        module.setName("Test Module");
        ArrayList<Long> parents = new ArrayList<>();
        parents.add((long)2);
        module.setParentsId(parents);

        Module module2 = new Module();
        module.setName("Test Module Parent");

        given(unitService.findOne(1)).willReturn(Optional.of(unit));
        given(moduleService.findOne(1)).willReturn(Optional.of(module));
        given(moduleService.findOne(2)).willReturn(Optional.of(module2));

        mvc.perform(MockMvcRequestBuilders.delete("/api/units/1/modules/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(unit.getModules().size(), is(0));
    }

    @Test
    public void testNotFoundDeleteModuleFromUnit() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/units/2/modules/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
