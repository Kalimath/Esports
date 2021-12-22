package com.example.backend;

import com.example.backend.DTO.ScoreDTO;
import com.example.backend.DTO.WedstrijdDTO;
import com.example.backend.model.*;
import com.example.backend.repo.*;
import com.example.backend.service.EmailSenderService;
import com.example.backend.service.RegistratieService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WedstrijdTests extends AbstractIntegrationTest {


    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private TeamDao teamDao;
    @Autowired
    private WedstrijdDao wedstrijdDao;
    @Autowired
    private TeamWedstrijdDAO teamWedstrijdDao;
    @Autowired
    private RegistratieService registratieService;
    @Autowired
    private SpelerDao spelerDAO;

    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Team teamA;
    private Team teamB;
    private WedstrijdDTO wedstrijdDTO;
    private String password = "apdf";

    @Autowired
    private EmailSenderService emailSenderService;
    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("duke", "springboot"))
            .withPerMethodLifecycle(false);

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(springSecurity()).build();
        teamA = teamDao.save(new Team.Builder().naam("teamA").build());
        teamB = teamDao.save(new Team.Builder().naam("teamB").build());

        //testspelers aanmaken
        Adres adres = new Adres.Builder()
                .gemeente("Leuven")
                .huisnr("5")
                .postcode("3000")
                .straat("Engels plein").build();


        Speler speler = new Speler.Builder()
                .naam("Vermeulen")
                .voornaam("Jos")
                .email("simon.fripon@hotmail.com")
                .geboortedatum(LocalDate.parse("2017-03-08"))
                .username("ddddd")
                .password(passwordEncoder.encode(password))
                .adres(adres).build();

        Speler speler2 = new Speler.Builder()
                .naam("Vermeulen")
                .voornaam("Jan")
                .email("r0661942@ucll.be")
                .geboortedatum(LocalDate.parse("2017-08-03"))
                .username("vvvv")
                .password(passwordEncoder.encode(password))
                .adres(adres).build();

        Speler savedSpeler = spelerDAO.save(speler);
        Speler savedSpeler2 = spelerDAO.save(speler2);

        //testspelers toewijzen aan teams
        registratieService.teamToewijzing(teamA.getId(),savedSpeler.getId());
        registratieService.teamToewijzing(teamB.getId(),savedSpeler2.getId());

        wedstrijdDTO = new WedstrijdDTO(teamA.getId(),teamB.getId(),LocalDateTime.now());
    }

    @Test
    void maaktWedstrijdAan() throws Exception{

        String passwoord = "wachtwoord";
        Manager manager = new Manager.Builder()
                .naam("mathias Clement")
                .email("mathias@gmail.com")
                .username("aaaaa")
                .password(passwordEncoder.encode(passwoord))
                .build();
        managerDAO.save(manager);
        String content = toJson(wedstrijdDTO);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/wedstrijden")
                        .with(httpBasic(manager.getUsername(),passwoord))
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        WedstrijdDTO wedstrijdRes = fromMvcResult(mvcResult, WedstrijdDTO.class);

          assertEquals(wedstrijdRes.getTeam_id_1(), teamA.getId());
          assertEquals(wedstrijdRes.getTeam_id_2(), teamB.getId());
    }

    @Test
    void alleWedstrijdenOpvragen() throws Exception {
        String passwoord = "wachtwoord";
        Manager manager = new Manager.Builder()
                .naam("mathias Clement")
                .email("mathias@gmail.com")
                .username("cccc")
                .password(passwordEncoder.encode(passwoord))
                .build();
        managerDAO.save(manager);
        //alle wedstrijden opvragen
        MvcResult mvcResult3 = this.mockMvc.perform(MockMvcRequestBuilders.get("/wedstrijden/dto")
                        .with(httpBasic(manager.getUsername(),passwoord))
                        .accept("application/json"))
                .andDo(print()).andReturn();

        //json(wedstrijden) uit db omzetten naar lijst van wedstrijdDTO's
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        List<Wedstrijd> wedstrijdenRes = mapper.readValue(mvcResult3.getResponse().getContentAsString(), new TypeReference<List<Wedstrijd>>() {});

        assertEquals(wedstrijdDao.findAll().size(), wedstrijdenRes.size());
    }

    @Test
    void voerScoreInVoorWedstrijd() throws Exception {

        String passwoord = "wachtwoord";
        Manager manager = new Manager.Builder()
                .naam("mathias Clement")
                .email("mathias@gmail.com")
                .username("ooooo")
                .password(passwordEncoder.encode(passwoord))
                .build();
        managerDAO.save(manager);
        String content = toJson(wedstrijdDTO);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/wedstrijden")
                        .with(httpBasic(manager.getUsername(),passwoord))
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        WedstrijdDTO wedstrijdRes = fromMvcResult(mvcResult, WedstrijdDTO.class);

        //create DTO to send in request
        ScoreDTO scores = new ScoreDTO(1,5, teamA.getId(),teamB.getId());

        String scoresJson = toJson(scores);
        MvcResult mvcResult2 = this.mockMvc.perform(MockMvcRequestBuilders.put("/wedstrijden/"+wedstrijdRes.getWedstrijd_id())
                        .with(httpBasic(manager.getUsername(),passwoord))
                        .content(scoresJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(teamWedstrijdDao.findTeam_wedstrijdByTeam_Id(scores.getTeamA()).get().getScore(), scores.getScoreTeamA());
        assertEquals(teamWedstrijdDao.findTeam_wedstrijdByTeam_Id(scores.getTeamB()).get().getScore(), scores.getScoreTeamB());
    }



    @Test
    void emailIsVerstuurdNaarElkeSpelerBijAanmaakVanEenWedstrijd() throws Exception {
        String passwoord = "wachtwoord";
        Manager manager = new Manager.Builder()
                .naam("mathias Clement")
                .email("mathias@gmail.com")
                .username("jjjj")
                .password(passwordEncoder.encode(passwoord))
                .build();
        managerDAO.save(manager);
        //wedstrijd aanmaken
        String content = toJson(wedstrijdDTO);
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/wedstrijden")
                        .with(httpBasic(manager.getUsername(),passwoord))
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    


}
