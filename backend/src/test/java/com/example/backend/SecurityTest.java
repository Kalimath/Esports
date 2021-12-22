package com.example.backend;

import com.example.backend.model.Adres;
import com.example.backend.model.Manager;
import com.example.backend.model.Speler;
import com.example.backend.repo.ManagerDAO;
import com.example.backend.repo.SpelerDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecurityTest extends AbstractIntegrationTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SpelerDao spelerDao;

    @Autowired
    private ManagerDAO managerDAO;



    @BeforeEach
    void setUp(){

        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .apply(springSecurity())
                .build();

        passwordEncoder = new BCryptPasswordEncoder();

    }
    @Test
    void getSpeler() throws Exception{


        String wachtwoord = "paswoord";
        Speler speler = new Speler.Builder()
                .voornaam("mathias")
                .naam("clement")
                .email("mathias@gmail.com")
                .geboortedatum(LocalDate.parse("1999-12-26"))
                .username("mathew")
                .password(passwordEncoder.encode(wachtwoord))
                .adres(new Adres.Builder()
                        .straat("voer")
                        .huisnr("3")
                        .postcode("3000")
                        .gemeente("leuven")
                        .build())
                .build();

        spelerDao.save(speler);

        String passwoord = "wachtwoord";
        Manager manager = new Manager.Builder()
                .naam("mathias Clement")
                .email("mathias@gmail.com")
                .username("mathew2")
                .password(passwordEncoder.encode(passwoord))
                .build();
        managerDAO.save(manager);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/spelers/"+ speler.getId())
                        .with(httpBasic(speler.getUsername(),wachtwoord))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }
}
