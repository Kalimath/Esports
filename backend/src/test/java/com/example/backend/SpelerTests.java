package com.example.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.backend.DTO.SpelerDTO;
import com.example.backend.model.Adres;
import com.example.backend.model.Speler;
import com.example.backend.repo.SpelerDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SpelerTests extends AbstractIntegrationTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    private SpelerDTO jos;


    @Autowired
    private SpelerDao spelerDao;


    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac) .apply(springSecurity()).build();

        //speler object initialiseren
        jos = new SpelerDTO("Jos","Vermeulen","jos.vermeulen@gmail.com",LocalDate.parse("1998-12-27"),"voer","1","3000", "Leuven","joske","admin");
    }

    @Test
    void maaktNieuweSpeler() throws Exception {

        String content = toJson(jos);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/spelers")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        Optional<Speler> speler = spelerDao.findFirstByUsername("joske");
        assertEquals("Jos", speler.get().getVoornaam());
        assertEquals("jos.vermeulen@gmail.com", speler.get().getEmail());
    }

    @Test
    void updateSpeler() throws Exception {

        //speler aanmaken in db
       this.mockMvc.perform(MockMvcRequestBuilders.post("/spelers")
                        .content(toJson(jos))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //speler aanpassen

        Optional<Speler> speler = spelerDao.findFirstByUsername("joske");

        SpelerDTO spelerDTO = new SpelerDTO("Jos","Vermeulen","update@test.be",LocalDate.parse("1998-12-27"),"voer","1","3000", "Leuven","joske","admin");
        //speler update versturen
         this.mockMvc.perform(MockMvcRequestBuilders.put("/spelers/"+speler.get().getId())
                         .with(httpBasic(speler.get().getUsername(),jos.getPassword()))
                        .content(toJson(spelerDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andReturn();


        Optional<Speler> speler1 = spelerDao.findById(speler.get().getId());


        assertEquals(speler1.get().getEmail(),"update@test.be");
    }

    @Test
    void lijstSpelersOpvragen() throws Exception{


        //spelers aanmaken in db
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/spelers")
                        .content(toJson(jos))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


            SpelerDTO jos2 = new SpelerDTO("Jos","Vermeulen","update@test.be",LocalDate.parse("1998-12-27"),"voer","1","3000", "Leuven","joido","admin");

           this.mockMvc.perform(MockMvcRequestBuilders.post("/spelers")
                            .content(toJson(jos2))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andReturn();


        //alle spelers opvragen
        MvcResult mvcResult3 = this.mockMvc.perform(MockMvcRequestBuilders.get("/spelers/")
                        .with(httpBasic(jos.getUsername(),jos.getPassword()))
                        .accept("application/json"))
                .andDo(print()).andReturn();



        //json(spelers) uit db omzetten naar lijst van spelerDTO's
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<Speler> alleSpelers = mapper.readValue(mvcResult3.getResponse().getContentAsString(), new TypeReference<List<Speler>>() {});

        assertEquals(2, alleSpelers.size());
    }




}
