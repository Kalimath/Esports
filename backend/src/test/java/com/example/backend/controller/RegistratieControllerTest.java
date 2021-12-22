package com.example.backend.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.backend.AbstractIntegrationTest;
import com.example.backend.DTO.RegistratieDTO;
import com.example.backend.DTO.SpelerDTO;
import com.example.backend.model.Speler;
import com.example.backend.model.Speler_team;
import com.example.backend.model.Team;
import com.example.backend.repo.SpelerDao;
import com.example.backend.repo.SpelerTeamDAO;
import com.example.backend.repo.TeamDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class RegistratieControllerTest extends AbstractIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TeamDao teamDao;

    @Autowired
    private SpelerDao spelerDao;

    @Autowired
    private SpelerTeamDAO spelerTeamDAO;

    private MockMvc mockMvc;

    private SpelerDTO jos;
    private Team team;

    private Team repTeam;
    private Speler repoJos;

    @BeforeEach
    void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        jos = new SpelerDTO("Jos",
                "Vermeulen",
                "jos.vermeulen@gmail.com",
                LocalDate.parse("1994-05-21"),
                "Molenstraat",
                "101",
                "3000",
                "Leuven",
                "joske",
                "admin"
        );

        team = new Team.Builder().naam("Team winnaars").build();


        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/spelers")
                .content(toJson(jos))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        SpelerDTO speler = fromMvcResult(mvcResult, SpelerDTO.class);
        System.out.println(speler.getNaam());

        MvcResult mvcResult1 =  this.mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                .content(toJson(team))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        repTeam = teamDao.findFirstByNaam("Team winnaars").get();
        repoJos = spelerDao.findFirstByVoornaamAndNaamIgnoreCase("Jos", "Vermeulen").get();

    }

    @Test
    void getRegistratiesTeam() throws Exception {
        //Given
        //In before each

        //When
        String registratiepost = String.format("/registraties/%d?spelerId=%d", repTeam.getId(), repoJos.getId());

        MvcResult mvcResultJosInTeam = this.mockMvc.perform(MockMvcRequestBuilders.post(registratiepost)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultList = this.mockMvc.perform(MockMvcRequestBuilders.get("/registraties/" + repTeam.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

       // List<Speler_team> speler_teamList = fromMvcResult(mvcResultList, List<Speler_team>);

        //Then
        /*assertEquals(repTeam.getId(), speler_teamList.stream().findFirst().get().getTeam().getId());
        assertEquals(repoJos.getId(), speler_teamList.stream().findFirst().get().getSpeler().getId());*/

        assertNotNull(spelerTeamDAO.findAll());
    }

    @Test
    void teamToewijzing() throws Exception {
        //Given
        //In before each

        //When
        String registratiepost = String.format("/registraties/%d?spelerId=%d", repTeam.getId(), repoJos.getId());

        MvcResult mvcResultJosInTeam = this.mockMvc.perform(MockMvcRequestBuilders.post(registratiepost)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultList = this.mockMvc.perform(MockMvcRequestBuilders.get("/registraties/" + repTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response =  mvcResultList.getResponse().getContentAsString();

        //Then
        System.out.println(response);
        Speler_team sp = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();
        assertNotNull(sp);
        assertEquals(repoJos , sp.getSpeler());
        assertEquals(repTeam, sp.getTeam());
        assertFalse(sp.getReserve());
    }

    @Test
    void reserveTeamToewijzing() throws Exception {
        //Given
        //In before each

        //When
        String registratiepost = "/registraties/reserve";
        RegistratieDTO dto = new RegistratieDTO();
        dto.setTeam_id(repTeam.getId());
        dto.setSpeler_id(repoJos.getId());
        dto.setIsreserve(true);

        MvcResult mvcResultJosInTeam = this.mockMvc.perform(MockMvcRequestBuilders.post(registratiepost)
                        .content(toJson(dto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultList = this.mockMvc.perform(MockMvcRequestBuilders.get("/registraties/" + repTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String response =  mvcResultList.getResponse().getContentAsString();

        //Then
        System.out.println(response);
        Speler_team sp = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();
        assertNotNull(sp);
        assertEquals(repoJos , sp.getSpeler());
        assertEquals(repTeam, sp.getTeam());
        assertTrue(sp.getReserve());
    }

    @Test
    void promoteToActive() throws Exception {

        String registratiepost = "/registraties/reserve";
        RegistratieDTO dto = new RegistratieDTO();
        dto.setTeam_id(repTeam.getId());
        dto.setSpeler_id(repoJos.getId());
        dto.setIsreserve(true);

        MvcResult mvcResultJosInTeam = this.mockMvc.perform(MockMvcRequestBuilders.post(registratiepost)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultList = this.mockMvc.perform(MockMvcRequestBuilders.get("/registraties/" + repTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Speler_team sp = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();


        MvcResult mvcResultTeamChange = this.mockMvc.perform(MockMvcRequestBuilders.put("/registraties/promote/" + sp.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println((mvcResultTeamChange.getResponse().getContentAsString()));
        //Then
        assertNotNull(sp);
        assertEquals(repoJos , sp.getSpeler());
        assertEquals(repTeam, sp.getTeam());
        assertFalse(sp.getReserve());
    }

    @Test
    void demoteToReserve() throws Exception {
        String registratiepost = String.format("/registraties/%d?spelerId=%d", repTeam.getId(), repoJos.getId());

        MvcResult mvcResultJosInTeam = this.mockMvc.perform(MockMvcRequestBuilders.post(registratiepost)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultList = this.mockMvc.perform(MockMvcRequestBuilders.get("/registraties/" + repTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Speler_team sp = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();

        MvcResult mvcResultTeamChange = this.mockMvc.perform(MockMvcRequestBuilders.put("/registraties/demote/" + sp.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println((mvcResultTeamChange.getResponse().getContentAsString()));

        //Then
        assertNotNull(sp);
        assertEquals(repoJos , sp.getSpeler());
        assertEquals(repTeam, sp.getTeam());
        assertTrue(sp.getReserve());
    }


    @Test
    void deleteSpelerFromTeam() throws Exception {
        //Given
        String registratiepost = "/registraties/reserve";
        RegistratieDTO dto = new RegistratieDTO();
        dto.setTeam_id(repTeam.getId());
        dto.setSpeler_id(repoJos.getId());
        dto.setIsreserve(false);

        MvcResult mvcResultJosInTeam = this.mockMvc.perform(MockMvcRequestBuilders.post(registratiepost)
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultList = this.mockMvc.perform(MockMvcRequestBuilders.get("/registraties/" + repTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Speler_team sp = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();

        //When

        MvcResult mvcResultDelete = this.mockMvc.perform(MockMvcRequestBuilders.delete("/registraties/delete/" + sp.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        //sp = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();
        assertThrows(NoSuchElementException.class, () -> {spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();});

    }

    @Test
    void deleteSpelerFromReserveTeam() throws Exception {
        //Given
        String registratiepost = "/registraties/reserve";
        RegistratieDTO dto = new RegistratieDTO();
        dto.setTeam_id(repTeam.getId());
        dto.setSpeler_id(repoJos.getId());
        dto.setIsreserve(true);

        MvcResult mvcResultJosInTeam = this.mockMvc.perform(MockMvcRequestBuilders.post(registratiepost)
                        .content(toJson(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult mvcResultList = this.mockMvc.perform(MockMvcRequestBuilders.get("/registraties/" + repTeam.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Speler_team sp = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();

        //When

        MvcResult mvcResultDelete = this.mockMvc.perform(MockMvcRequestBuilders.delete("/registraties/delete/" + sp.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        //sp = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();
        assertThrows(NoSuchElementException.class, () -> {spelerTeamDAO.findSpeler_teamBySpelerAndTeam(repoJos, repTeam).get();});
    }
}