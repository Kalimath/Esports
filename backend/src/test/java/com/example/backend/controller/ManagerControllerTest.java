package com.example.backend.controller;

import com.example.backend.AbstractIntegrationTest;
import com.example.backend.DTO.ManagerDTO;
import com.example.backend.model.Manager;
import com.example.backend.model.Speler;
import com.example.backend.repo.ManagerDAO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import liquibase.pro.packaged.M;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


class ManagerControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private ManagerDAO managerDAO;

    //Variabelen
    private final int AMOUNT = 8;
    private Manager gemaakteManager;
    private Manager manager;
    private Manager aangepasteManager;
    private Manager getAangepasteManager;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
    }

    @Test
    void getAllManagers() throws Exception {
        //Given
        for (int i = 1; i <= AMOUNT; i++){
            Manager manager = new Manager.Builder()
                    .naam("Jos" + i)
                    .email("Jos" + i + ".vermeulen@gmail.com")
                    .build();

            MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                            .content(toJson(manager))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andReturn();
        }
        //When
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/managers").accept("application/json"))
                .andDo(print()).andReturn();

        //Then
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<Manager> alleManagers = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Manager>>() {});

        assertEquals(AMOUNT, alleManagers.size());
    }

    @Test
    void createManager() throws Exception {
        //Given
         ManagerDTO dto = new ManagerDTO();
         dto.setNaam("Jos");
         dto.setEmail("Jos.vermeulen@gmail.com");
         dto.setUsername("JV");
         dto.setPassword("t");

        //When
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Optional<Manager> mananger =  managerDAO.findFirstByUsername("JV");
        assertEquals(dto.getNaam(), mananger.get().getNaam());
    }

    @Test
    void getById() throws Exception {
        //Given
        ManagerDTO dto = new ManagerDTO();
        dto.setNaam("Jos");
        dto.setEmail("Jos.vermeulen@gmail.com");
        dto.setUsername("JV");
        dto.setPassword("t");

        //When
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Optional<Manager> managerJos =  managerDAO.findFirstByUsername("JV");
        assertEquals(dto.getNaam(), managerJos.get().getNaam());

        MvcResult mvcGetResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/managers/" + managerJos.get().getId())
                        .with(httpBasic(dto.getUsername(), "t"))
                        .accept("application/json"))
                .andDo(print()).andReturn();

        getAangepasteManager = fromMvcResult(mvcGetResult, Manager.class);

        //Then
        Optional<Manager> managerDAO = this.managerDAO.findById(managerJos.get().getId());

        assertEquals(managerJos.get().getNaam(), getAangepasteManager.getNaam());
        assertEquals(getAangepasteManager.getNaam(), managerDAO.get().getNaam());
    }

    @Test
    void updateManager() throws Exception {
        //Given
        ManagerDTO dto = new ManagerDTO();
        dto.setNaam("Jos");
        dto.setEmail("Jos.vermeulen@gmail.com");
        dto.setUsername("Joske");
        dto.setPassword("t");

        ManagerDTO dtoUpdate = new ManagerDTO();
        dto.setNaam("Jeff");
        dto.setEmail("Jeff.vermeulen@gmail.com");
        dto.setUsername("Jeffke");

        //When
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/managers")
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Optional<Manager> managerJos =  managerDAO.findFirstByUsername("Joske");
        assertEquals(dto.getNaam(), managerJos.get().getNaam());


        MvcResult mvcUpdateResult = this.mockMvc.perform(MockMvcRequestBuilders.put("/managers/" + managerJos.get().getId())
                        .with(httpBasic(dto.getUsername(), "t"))
                        .content(toJson(dtoUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcGetResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/managers/" + managerJos.get().getId())
                        .with(httpBasic(dto.getUsername(), "t"))
                        .accept("application/json"))
                .andDo(print()).andReturn();

        Optional<Manager> updatedManager = managerDAO.findFirstByUsername("Jeffke");

        //Then
        assertNotEquals(managerJos.get(), updatedManager.get());
        assertEquals(updatedManager.get().getNaam(), dtoUpdate.getNaam());
        assertEquals(updatedManager.get().getUsername(), dtoUpdate.getUsername());
        assertEquals(updatedManager.get().getEmail(), dtoUpdate.getEmail());
    }
}