package com;

import com.google.gson.Gson;
import com.itinerary.block.Block;
import com.itinerary.block.BlockService;
import com.itinerary.module.Module;
import com.itinerary.module.ModuleService;
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
public class ModuleApiTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ModuleService moduleService;

    @MockBean
    private BlockService blockService;

    private Gson jsonParser = new Gson();

    @Before
    public void initialize() {
        Module module = new Module();
        module.setName("Test Module");
        ArrayList<Module> modules = new ArrayList<>();
        modules.add(module);

        given(moduleService.findAll()).willReturn(modules);
        given(moduleService.findOne(1)).willReturn(Optional.of(module));
        given(moduleService.findOne(2)).willReturn(Optional.empty());
    }

    @Test
    public void testGetModules() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/modules/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Test Module")));
    }

    @Test
    public void testGetModule() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/modules/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Module")));
    }

    @Test
    public void testNotFoundGetModule() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/modules/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    public void testUpdateModule() throws Exception{
        Module module2 = new Module();
        module2.setName("Test Module 2");

        mvc.perform(MockMvcRequestBuilders.put("/api/modules/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(module2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Module 2")));
    }

    @Test
    public void testNotFoundUpdateModule() throws Exception{
        Module module2 = new Module();
        module2.setName("Test Module 2");

        mvc.perform(MockMvcRequestBuilders.put("/api/modules/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(module2)))
                .andExpect(status().is(404));
    }

    @Test
    public void testAddBlockModule() throws Exception{
        Module module = new Module();
        module.setId(1);
        module.setName("Test Module");

        Block block = new Block();
        block.setName("Test Block");

        given(moduleService.findOne(1)).willReturn(Optional.of(module));
        given(blockService.findOne(0)).willReturn(Optional.of(block));

        mvc.perform(MockMvcRequestBuilders.post("/api/modules/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonParser.toJson(block)))
                .andExpect(status().is(201));

        assertThat(module.getBlocks().size(), is(1));
    }

    @Test
    public void testDeleteBlockFromModule() throws Exception{
        Module module = new Module();
        module.setId(1);
        module.setName("Test Module");

        Block block = new Block();
        block.setId(1);
        block.setName("Test Block");

        given(moduleService.findOne(1)).willReturn(Optional.of(module));
        given(blockService.findOne(1)).willReturn(Optional.of(block));

        mvc.perform(MockMvcRequestBuilders.delete("/api/modules/1/blocks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(module.getBlocks().size(), is(0));
    }

    @Test
    public void testNotFoundDeleteBlockFromModule() throws Exception{
        mvc.perform(MockMvcRequestBuilders.delete("/api/modules/2/blocks/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
