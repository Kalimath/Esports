package com.example.backend;

import com.example.backend.DTO.TeamDTO;
import com.example.backend.model.Adres;
import com.example.backend.model.Manager;
import com.example.backend.model.Speler;
import com.example.backend.model.Team;
import com.example.backend.repo.ManagerDAO;
import com.example.backend.repo.SpelerDao;
import com.example.backend.repo.TeamDao;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TeamTest extends AbstractIntegrationTest{

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private ManagerDAO managerDAO;

    private MockMvc mockMvc;
    private Team mamajosi;
    private Team gemaaktTeam;
    private Team updateTeam;
    private TeamDTO G2;
    private final int AMOUNT = 3;


    @Autowired
    private TeamDao teamDao;

    @BeforeEach
    public void setUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        this.gemaaktTeam = null;

    }

   @Test
   public void maakNieuwTeam() throws Exception {
       mamajosi = new Team.Builder().naam("mamajosi").build();
       String passwoord = "wachtwoord";
       Manager manager = new Manager.Builder()
               .naam("mathias Clement")
               .email("mathias@gmail.com")
               .username("teste")
               .password(passwordEncoder.encode(passwoord))
               .build();
       managerDAO.save(manager);

       String content = toJson(mamajosi);
       MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                       .with(httpBasic(manager.getUsername(),passwoord))
                       .content(content)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.id").exists())
               .andExpect(status().isOk())
               .andReturn();

       gemaaktTeam = fromMvcResult(mvcResult,Team.class);

       Optional<Team> team = teamDao.findById(gemaaktTeam.getId());
       assertEquals(mamajosi.getNaam(), team.get().getNaam());
   }

   @Test
    public void updateTeamNaam() throws Exception{
       String passwoord = "wachtwoord";
       Manager manager = new Manager.Builder()
               .naam("mathias Clement")
               .email("mathias@gmail.com")
               .username("tesenisleuk")
               .password(passwordEncoder.encode(passwoord))
               .build();
       managerDAO.save(manager);
        updateTeam = new Team.Builder().naam("bla").build();
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .with(httpBasic(manager.getUsername(),passwoord))
                       .content(toJson(updateTeam))
                       .contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.id").exists())
               .andExpect(status().isOk())
               .andReturn();
        gemaaktTeam = fromMvcResult(mvcResult,Team.class);



        String nieuweNaam = "G2";
        gemaaktTeam.setNaam(nieuweNaam);
        MvcResult mvcResult2 = this.mockMvc.perform(MockMvcRequestBuilders.put("/teams/"+ gemaaktTeam.getId())
                        .with(httpBasic(manager.getUsername(),passwoord))
                       .content(toJson(updateTeam))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn();

        assertEquals(gemaaktTeam.getNaam(),nieuweNaam);
   }

   @Test
    public void lijstTeamsOpvragen() throws Exception{
       String passwoord = "wachtwoord";
       Manager manager = new Manager.Builder()
               .naam("mathias Clement")
               .email("mathias@gmail.com")
               .username("kkkkk")
               .password(passwordEncoder.encode(passwoord))
               .build();
       managerDAO.save(manager);
       mamajosi = new Team.Builder().naam("mamajosi").build();
       MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                       .with(httpBasic(manager.getUsername(),passwoord))
                       .content(toJson(mamajosi))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").exists())
               .andReturn();


           Team g2 = new Team.Builder().naam("g2").build();

           MvcResult mvcResult2 = this.mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                           .with(httpBasic(manager.getUsername(),passwoord))
                           .content(toJson(g2))
                           .contentType(MediaType.APPLICATION_JSON))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.id").exists())
                   .andReturn();

       MvcResult mvcResult3 = this.mockMvc.perform(MockMvcRequestBuilders.get("/teams").accept("application/json"))
               .andDo(print())
               .andReturn();



       List<Team> alleTeams = teamDao.findAll();


       assertTrue(alleTeams.size()>0);

   }
}
