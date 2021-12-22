package com.example.backend.service;

import com.example.backend.DTO.SpelerDTO;
import com.example.backend.config.SpelerPrincipal;
import com.example.backend.exceptions.SpelerZitReedsInTeam;
import com.example.backend.model.Adres;
import com.example.backend.model.Speler;
import com.example.backend.exceptions.SpelerNotFound;
import com.example.backend.exceptions.TeamNotFound;
import com.example.backend.model.Speler_team;
import com.example.backend.model.Team;
import com.example.backend.repo.AdresDAO;
import com.example.backend.repo.SpelerDao;
import com.example.backend.repo.SpelerTeamDAO;
import com.example.backend.repo.TeamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpelerService {

    private final SpelerDao spelerDao;
    private final AdresDAO adresDAO;
    private final TeamDao teamDao;
    private final SpelerTeamDAO spelerTeamDAO;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    public SpelerService(SpelerDao spelerDao, AdresDAO adresDAO, TeamDao teamDao, SpelerTeamDAO spelerTeamDAO) {
        this.spelerDao = spelerDao;
        this.adresDAO = adresDAO;
        this.teamDao = teamDao;
        this.spelerTeamDAO = spelerTeamDAO;
    }

    /**
     * @param spelerDTO waardes die opgeslagen moeten worden
     *
     * @return de opgeslagen speler
     */
    public SpelerDTO createSpeler(SpelerDTO spelerDTO){
        Optional<Adres> adresBestaat= adresDAO.findAdresByGemeenteAndHuisnrAndPostcodeAndStraat(spelerDTO.getGemeente(),spelerDTO.getHuisnummer(),spelerDTO.getPostcode(),spelerDTO.getStraat());
        Adres adres = new Adres.Builder()
                .gemeente(spelerDTO.getGemeente())
                .huisnr(spelerDTO.getHuisnummer())
                .postcode(spelerDTO.getPostcode())
                .straat(spelerDTO.getStraat()).build();

        Speler speler = new Speler.Builder()
                .naam(spelerDTO.getNaam())
                .voornaam(spelerDTO.getVoornaam())
                .email(spelerDTO.getEmail())
                .geboortedatum(spelerDTO.getGeboortedatum())
                .username(spelerDTO.getUsername())
                .password(passwordEncoder.encode(spelerDTO.getPassword()))
                .adres(adresBestaat.orElse(adres)).build();
        spelerDao.save(speler);
        spelerDTO.setId(speler.getId());
        return spelerDTO;
    }

    /**
     * Pas een speler aan
     * @param id van speler
     * @param spelerDTO aangepaste waardes
     */
    public void update(long id, SpelerDTO spelerDTO){
        Optional<Speler> dbSpeler = getById(id);
        Optional<Adres> dbAdres = adresDAO.findAdresByGemeenteAndHuisnrAndPostcodeAndStraat(spelerDTO.getGemeente(),spelerDTO.getHuisnummer(),spelerDTO.getPostcode(),spelerDTO.getStraat());
        Adres adres = new Adres.Builder()
                .gemeente(spelerDTO.getGemeente())
                .huisnr(spelerDTO.getHuisnummer())
                .postcode(spelerDTO.getPostcode())
                .straat(spelerDTO.getStraat()).build();

        if (dbSpeler.isPresent()) {
            dbSpeler.get().setNaam(spelerDTO.getNaam());
            dbSpeler.get().setVoornaam(spelerDTO.getVoornaam());
            dbSpeler.get().setEmail(spelerDTO.getEmail());
            dbSpeler.get().setGeboortedatum(spelerDTO.getGeboortedatum());
            dbSpeler.get().setUsername(spelerDTO.getUsername());
            dbSpeler.get().setAdres(dbAdres.orElse(adres));
        }
    }

    /**
     * @param id
     * @return haal een speler op op basis van een id
     */
    public Optional<Speler> getById(long id){
        return spelerDao.findById(id);
    }


    /**
     * @return geeft alle spelers terug
     */
    public Optional<List<Speler>> getAllSpelers(){
        return Optional.of(spelerDao.findAll());
    }

    /**
     * @param team_id
     * @return geeft alle actieve spelers terug van een team
     */
    public Optional<List<Speler_team>> getActieveSpelersVanEenTeam(long team_id){
        Optional<Team> team = teamDao.findById(team_id);

        return spelerTeamDAO.findSpeler_teamsByTeamAndIsreserve(team.get(),false);
    }

    /**
     * @param team_id
     * @return geeft alle reserve spelers terug van een team
     */
    public Optional<List<Speler_team>> getReserveSpelersVanEenTeam(long team_id){
        Optional<Team> team = teamDao.findById(team_id);

        return spelerTeamDAO.findSpeler_teamsByTeamAndIsreserve(team.get(),true);
    }

    /**
     * @param team_id
     * @return alle spelers die niet in dit team zitten
     */
    public Optional<List<Speler>> getSpelersNietInTeam(long team_id){
        Optional<Team> team = teamDao.findById(team_id);
        if (team.isEmpty()){return Optional.empty();}
        List<Speler> spelers = spelerDao.findAll();
        List<Speler> spelersNietINTeam = new ArrayList<>();
        for (Speler speler: spelers){
            Optional<Speler_team> speler_team = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler,team.get());
            if (speler_team.isEmpty()){
                spelersNietINTeam.add(speler);
            }
        }
        return Optional.of(spelersNietINTeam);

    }

    /**
     * @param voornaam
     * @param naam
     * @return een speler op basis van de 2 parameters
     */
    public Optional<Speler> getSpelerByVoornaamAndNaam(String voornaam, String naam){
        return spelerDao.findFirstByVoornaamAndNaamIgnoreCase(voornaam, naam);
    }

    /**
     * @return geeft de ingelogde speler terug
     */
    public Optional<Speler> getIngelogdeSpeler(){
        SpelerPrincipal principal = (SpelerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Speler speler = principal.getSpeler();
        return Optional.ofNullable(speler);
    }


}
