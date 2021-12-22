package com.example.backend.service;

import com.example.backend.DTO.RegistratieDTO;
import com.example.backend.exceptions.SpelerNotFound;
import com.example.backend.exceptions.SpelerToewijzingFout;
import com.example.backend.exceptions.SpelerZitReedsInTeam;
import com.example.backend.exceptions.TeamNotFound;
import com.example.backend.model.*;
import com.example.backend.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class RegistratieService {
    private final SpelerTeamDAO spelerTeamDAO;
    private final SpelerDao spelerDao;
    private final TeamDao teamDao;
    private final WedstrijdDao wedstrijdDao;
    private final TeamWedstrijdDAO teamWedstrijdDAO;
    private final PersoonlijkMatchHistoriekDAO persoonlijkMatchHistoriekDAO;
    private WedstrijdService wedstrijdService;

    @Autowired
    public RegistratieService(SpelerTeamDAO spelerTeamDAO, SpelerDao spelerDao, TeamDao teamDao, WedstrijdDao wedstrijdDao, TeamWedstrijdDAO teamWedstrijdDAO, PersoonlijkMatchHistoriekDAO persoonlijkMatchHistoriekDAO,WedstrijdService wedstrijdService) {
        this.spelerTeamDAO = spelerTeamDAO;
        this.spelerDao = spelerDao;
        this.teamDao = teamDao;
        this.wedstrijdDao = wedstrijdDao;
        this.teamWedstrijdDAO = teamWedstrijdDAO;
        this.persoonlijkMatchHistoriekDAO = persoonlijkMatchHistoriekDAO;
        this.wedstrijdService = wedstrijdService;
    }

    /**
     * @param id team
     * @return geef alle registraties terug van een team
     */
    public Optional<List<Speler_team>> getRegistratiesTeam(long id) {
        Optional<Team> team = teamDao.findById(id);
        if (team.isPresent()){
            return spelerTeamDAO.findSpeler_teamsByTeam(team.get());
        }
        return Optional.empty();
    }
    public ResponseEntity<String> teamToewijzing(Long id, Long spelerId) {
        Optional<Speler> speler = spelerDao.findById(spelerId);
        Optional<Team> team = teamDao.findById(id);

        try {
            if(speler.isPresent()){
                if(team.isPresent()){
                    Optional<Speler_team> speler_team = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler.get(), team.get());
                    if (speler_team.isEmpty()) {
                        Speler_team sp = new Speler_team.Builder()
                                .speler(speler.get())
                                .team(team.get())
                                .isreserve(false)
                                .build();
                        spelerTeamDAO.save(sp);
                    } else {
                        throw new SpelerZitReedsInTeam("Speler zit reeds in team");
                    }
                } else {
                    throw new TeamNotFound("Team niet gevonden");
                }
            } else
            {
                throw new SpelerNotFound("Speler niet gevonden");
            }
        }
        catch (SpelerNotFound | TeamNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (SpelerZitReedsInTeam e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param registratieDTO
     * @return maakt een registratie aan voor een team en speler
     */
    public ResponseEntity<String> reserveTeamToewijzing(RegistratieDTO registratieDTO) {
        Optional<Speler> speler = spelerDao.findById(registratieDTO.getSpeler_id());
        Optional<Team> team = teamDao.findById(registratieDTO.getTeam_id());

        try {
            if(speler.isPresent()){
                if(team.isPresent()){
                    Optional<Speler_team> speler_team = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler.get(), team.get());
                    if (speler_team.isEmpty()) {
                        Speler_team sp = new Speler_team.Builder()
                                .speler(speler.get())
                                .team(team.get())
                                .isreserve(registratieDTO.getIsreserve())
                                .build();

                        spelerTeamDAO.save(sp);

                        promoteToActive(spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler.get(),team.get()).get().getId());

                    } else {
                        throw new SpelerZitReedsInTeam("Speler zit al in dit team");
                    }
                } else {
                    throw new TeamNotFound("Team niet gevonden");
                }
            } else {
                throw new SpelerNotFound("Spelen niet gevonden");
            }
        } catch (TeamNotFound | SpelerNotFound e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (SpelerZitReedsInTeam e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param id
     * @return zet speler in de actieve speler lijst en voeg de nodig wedstrijden toe aan persoonlijke match historiek
     */
    public ResponseEntity<String> promoteToActive(Long id){
        Optional<Speler_team> speler_team = spelerTeamDAO.findById(id);
        Optional<List<Team_wedstrijd>> team_wedstrijds = teamWedstrijdDAO.findTeam_wedstrijdsByTeam(speler_team.get().getTeam());
        List<Wedstrijd> wedstrijden = new ArrayList<>();
        team_wedstrijds.get().forEach((team_wedstrijds1 -> {
           if (team_wedstrijds1.getWedstrijd().getTijdstip().isAfter(LocalDateTime.now())){
               wedstrijden.add(team_wedstrijds1.getWedstrijd());
           }}));

        if(speler_team.isPresent()){
            speler_team.get().setreserve(false);
            spelerTeamDAO.save(speler_team.get());
        }
        wedstrijden.forEach(wedstrijd -> {
            List<Team_wedstrijd> teamWedstrijds =  teamWedstrijdDAO.findTeam_wedstrijdsByWedstrijd(wedstrijd).get();
            wedstrijdService.addWedstrijdenAanPersoonlijkHistoriekVanSpeler(teamWedstrijds.get(0).getTeam(),teamWedstrijds.get(0), teamWedstrijds.get(1));
            wedstrijdService.addWedstrijdenAanPersoonlijkHistoriekVanSpeler(teamWedstrijds.get(1).getTeam(),teamWedstrijds.get(0), teamWedstrijds.get(1));

        });


        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param id
     * @return zet een speler in de reserve lijst en verwijder de nodige wedstrijden in de persoonlijke match historiek
     */
    public ResponseEntity<String> demoteToReserve(Long id){
        Optional<Speler_team> speler_team = spelerTeamDAO.findById(id);

        if(speler_team.isPresent()){
            speler_team.get().setreserve(true);
            Optional<List<PersoonlijkMatchHistoriek>> persoonlijkMatchHistorieken = persoonlijkMatchHistoriekDAO.findPersoonlijkMatchHistoriekBySpeler(speler_team.get().getSpeler());
            for (PersoonlijkMatchHistoriek persoonlijkMatchHistoriek: persoonlijkMatchHistorieken.get()){
                if (persoonlijkMatchHistoriek.getTeamWedstrijd().getTeam() == speler_team.get().getTeam() && (persoonlijkMatchHistoriek.getTeamWedstrijd().getWedstrijd().getTijdstip().isAfter(LocalDateTime.now()))){
                    if (findOtherPersoonlijkMatchHistoriek(speler_team.get(),persoonlijkMatchHistoriek).isPresent()) {
                        persoonlijkMatchHistoriekDAO.deleteAll(findOtherPersoonlijkMatchHistoriek(speler_team.get(), persoonlijkMatchHistoriek).get());
                    }

                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param speler_team
     * @param persoonlijkMatchHistoriek
     * @return zoekt een persoonlijkeMatchHistoriek op
     */
    private Optional<List<PersoonlijkMatchHistoriek>> findOtherPersoonlijkMatchHistoriek(Speler_team speler_team, PersoonlijkMatchHistoriek persoonlijkMatchHistoriek){

        Optional<List<Team_wedstrijd>> team_wedstrijden = teamWedstrijdDAO.findTeam_wedstrijdsByWedstrijd(persoonlijkMatchHistoriek.getTeamWedstrijd().getWedstrijd());

        List<PersoonlijkMatchHistoriek> persoonlijkMatchHistorieks = new ArrayList<>();
        if (spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler_team.getSpeler(),team_wedstrijden.get().get(0).getTeam()).isPresent() && !spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler_team.getSpeler(),team_wedstrijden.get().get(0).getTeam()).get().getReserve()
                && spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler_team.getSpeler(),team_wedstrijden.get().get(1).getTeam()).isPresent() && !spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler_team.getSpeler(),team_wedstrijden.get().get(1).getTeam()).get().getReserve()){return Optional.empty();}
        for (Team_wedstrijd team_wedstrijd: team_wedstrijden.get()){

          persoonlijkMatchHistorieks.add(persoonlijkMatchHistoriekDAO.findPersoonlijkMatchHistoriekByTeamWedstrijd(team_wedstrijd).get());
        }

        return Optional.of(persoonlijkMatchHistorieks);

    }

    /**
     * @param spelerteamid
     * @return verwijder de registratie op basis van id
     */
    public ResponseEntity<String> deleteSpelerFromTeam(Long spelerteamid) {

        try {
            if(spelerTeamDAO.findById(spelerteamid).isPresent()){
                //verwijder alle persoonlijke matchen in speler die nog moeten gebeuren in de toekomst
                demoteToReserve(spelerteamid);
                spelerTeamDAO.deleteById(spelerteamid);
            } else {
                throw new SpelerToewijzingFout("Speler zit niet in team is niet gevonden");
            }
        }
        catch (SpelerToewijzingFout e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * @param id
     * @return geeft een registratie terug tussen een speler en een team
     */
    public Optional<Speler_team> getSpelerTeamById(long id) {
        return spelerTeamDAO.findById(id);
    }
}
