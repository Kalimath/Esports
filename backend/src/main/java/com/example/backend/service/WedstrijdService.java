package com.example.backend.service;

import com.example.backend.DTO.ScoreDTO;
import com.example.backend.DTO.WedstrijdDTO;
import com.example.backend.DTO.WedstrijdListDTO;
import com.example.backend.exceptions.SpelerNotFound;
import com.example.backend.exceptions.TeamNotFound;
import com.example.backend.exceptions.WedstrijdFailure;
import com.example.backend.model.*;
import com.example.backend.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WedstrijdService {
    private final WedstrijdDao wedstrijdDao;
    private final TeamDao teamDao;
    private final SpelerDao spelerDao;
    private final SpelerTeamDAO spelerTeamDAO;
    private final TeamWedstrijdDAO teamWedstrijdDAO;
    private final PersoonlijkMatchHistoriekDAO persoonlijkMatchHistoriekDAO;
    private final EmailSenderService emailService;


    @Autowired
    public WedstrijdService(WedstrijdDao wedstrijdDao, TeamDao teamDao, SpelerDao spelerDao1, SpelerTeamDAO spelerDao, TeamWedstrijdDAO teamWedstrijdDAO, PersoonlijkMatchHistoriekDAO persoonlijkMatchHistoriekDAO, EmailSenderService emailService) {
        this.wedstrijdDao = wedstrijdDao;
        this.teamDao = teamDao;
        this.spelerDao = spelerDao1;
        this.spelerTeamDAO = spelerDao;
        this.teamWedstrijdDAO = teamWedstrijdDAO;
        this.persoonlijkMatchHistoriekDAO = persoonlijkMatchHistoriekDAO;
        this.emailService = emailService;
    }

    /**
     * @param wedstrijdDTO wedstrijd die aangemaakt moet worden
     * @return de aangemaakte wedstrijd
     * @throws TeamNotFound wedstrijden aanmaken mislukt
     */
    public WedstrijdDTO createWedstrijd(WedstrijdDTO wedstrijdDTO) throws TeamNotFound {
        //nieuwe wedstrijd aanmaken
        Wedstrijd nieuweWedstrijd = new Wedstrijd.Builder()
                .tijdstip(wedstrijdDTO.getTijdstip())
                .build();

        try {
            //teams ophalen uit database
            Optional<Team> teamA = teamDao.findById(wedstrijdDTO.getTeam_id_1());
            Optional<Team> teamB = teamDao.findById(wedstrijdDTO.getTeam_id_2());

            Team_wedstrijd team_wedstrijd1 = new Team_wedstrijd.Builder()
                    .wedstrijd(nieuweWedstrijd).team(teamA.get()).build();
            teamWedstrijdDAO.save(team_wedstrijd1);
            //Voeg de team_wedstrijd aan alle niet reserve spelers toe

            Team_wedstrijd team_wedstrijd2 = new Team_wedstrijd.Builder()
                    .wedstrijd(nieuweWedstrijd).team(teamB.get()).build();
            teamWedstrijdDAO.save(team_wedstrijd2);
            //Voeg de team_wedstrijden aan alle niet reserve spelers toe
            addWedstrijdenAanPersoonlijkHistoriekVanSpeler(teamA.get(),team_wedstrijd1,team_wedstrijd2);
            addWedstrijdenAanPersoonlijkHistoriekVanSpeler(teamB.get(),team_wedstrijd1,team_wedstrijd2);

        }catch (Exception e){
            throw new TeamNotFound("Wedstrijd aanmaken mislukt.");
        }

        //opslagen in db
        wedstrijdDao.save(nieuweWedstrijd);
        wedstrijdDTO.setWedstrijd_id(nieuweWedstrijd.getId());

        //breng spelers via mail op de hoogte
        List<Speler> ingeschrevenSpelers = new ArrayList<>();
        try{
            ingeschrevenSpelers = getSpelersInWedstrijd(nieuweWedstrijd);
            emailService.sendEmailNaarSpelersVoorWedstrijd(ingeschrevenSpelers, nieuweWedstrijd);
        }catch (Exception e){
            throw new WedstrijdFailure("spelers opvragen van wedstrijd mislukt");
        }


        return wedstrijdDTO;
    }


    /**
     * @param wedstrijdId waarmee naar een wedstrijd wordt gezocht
     * @param scoreDTO de dto die de scores en de teams bevatten
     * @return true or false als het gelukt of niet gelukt is
     */
    public boolean setWedstrijdscoreVoorTeam(long wedstrijdId, ScoreDTO scoreDTO) {
        try {
            Wedstrijd wedstrijd = wedstrijdDao.findById(wedstrijdId).get();
            List<Team_wedstrijd> teamsVanWedstrijd = teamWedstrijdDAO.findTeam_wedstrijdsByWedstrijd(wedstrijd).get();
            if(teamsVanWedstrijd.size()==2){
                if (scoreDTO.getTeamA() != 0 && teamsVanWedstrijd.get(0).getTeam().getId() == scoreDTO.getTeamA()) {
                    teamsVanWedstrijd.get(0).setScore(scoreDTO.getScoreTeamA());
                    teamWedstrijdDAO.save(teamsVanWedstrijd.get(0));
                    teamsVanWedstrijd.get(1).setScore(scoreDTO.getScoreTeamB());
                    teamWedstrijdDAO.save(teamsVanWedstrijd.get(1));
                } else if (scoreDTO.getTeamB() != 0 && teamsVanWedstrijd.get(1).getTeam().getId() == scoreDTO.getTeamB()){
                    teamsVanWedstrijd.get(0).setScore(scoreDTO.getScoreTeamB());
                    teamWedstrijdDAO.save(teamsVanWedstrijd.get(0));
                    teamsVanWedstrijd.get(1).setScore(scoreDTO.getScoreTeamA());
                    teamWedstrijdDAO.save(teamsVanWedstrijd.get(1));
                }else{
                    throw new TeamNotFound("teamId niet verbonden met wedstrijd met id "+wedstrijdId);
                }
            }else {
                throw new TeamNotFound("teams van wedstrijd is niet gelijk aan 2");
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //geeft alle spelers die meedoen aan een specifieke westrijd terug
    public List<Speler> getSpelersInWedstrijd(Wedstrijd w){
        List<Speler> spelersVanWedstrijd = new ArrayList<>();
        List<Speler_team> speler_teams = new ArrayList<>();
        List<Team_wedstrijd> teamsWedstrijd = teamWedstrijdDAO.findTeam_wedstrijdsByWedstrijd(w).get();
        for (Team_wedstrijd tw: teamsWedstrijd) speler_teams.addAll(spelerTeamDAO.findSpeler_teamsByTeam(tw.getTeam()).get());
        for (Speler_team st: speler_teams) spelersVanWedstrijd.add(st.getSpeler());
        return spelersVanWedstrijd;
    }

    /**
     * @return een list van wedstrijdLIstDTO die alle wedstrijden bevatten
     */
    public Optional<List<WedstrijdListDTO>> getWedstrijden(){
        List<Wedstrijd> wedstrijden = wedstrijdDao.findAll();
        return makeWedstrijdListDTO(wedstrijden);
    }

    /**
     * @param team_id id van team waarvan de match historiek moet opgehaald worden
     * @return een list van WedstrijdListDTO die de match historiek voorstelt
     */
    public Optional<List<WedstrijdListDTO>> getMatchHistoriekTeam(long team_id){
        Optional<Team> team = teamDao.findById(team_id);
        List<Wedstrijd> wedstrijden = new ArrayList<>();
        if (team.isPresent()){
          Optional<List<Team_wedstrijd>> team_wedstrijds = teamWedstrijdDAO.findTeam_wedstrijdsByTeam(team.get());
          for (Team_wedstrijd team_wedstrijd: team_wedstrijds.get()){
              wedstrijden.add(team_wedstrijd.getWedstrijd());
          }
        }
        return makeWedstrijdListDTO(wedstrijden);
    }

    /**
     * @param speler_id id van de speler waar de match historiek van moet worden opgehaald
     * @return een list van WedstrijdListDTO die de match historiek voorstelt
     */
    public Optional<List<WedstrijdListDTO>> getPersoonlijkMatchHistoriek(long speler_id){
        Optional<Speler> speler = spelerDao.findById(speler_id);
        if (speler.isEmpty()){
            return Optional.empty();
        }
        Set<Wedstrijd> wedstrijdenZonderDuplicaten = new HashSet<>();
        Optional<List<PersoonlijkMatchHistoriek>> persoonlijkMatchHistorieks = persoonlijkMatchHistoriekDAO.findPersoonlijkMatchHistoriekBySpeler(speler.get());
        for (PersoonlijkMatchHistoriek persoonlijkMatchHistoriek : persoonlijkMatchHistorieks.get()){
            wedstrijdenZonderDuplicaten.add(persoonlijkMatchHistoriek.getTeamWedstrijd().getWedstrijd());
        }

        List<Wedstrijd> wedstrijden = new ArrayList<>();
        wedstrijden.addAll(wedstrijdenZonderDuplicaten);
        return makeWedstrijdListDTO(wedstrijden);
    }

    /**
     * @param wedstrijden lijst die omgezet moet worden naar de WedstrijdDTO list
     * @return een op localDateTime geordende list van WedstrijdDTO
     */
    private Optional<List<WedstrijdListDTO>> makeWedstrijdListDTO(List<Wedstrijd> wedstrijden){
        List<WedstrijdListDTO> frontendList = new ArrayList<>();

        for (Wedstrijd wedstrijd: wedstrijden){
            Optional<List<Team_wedstrijd>> teamsVanWedstrijd = Optional.of(teamWedstrijdDAO.findTeam_wedstrijdsByWedstrijd(wedstrijd).get());
            if (teamsVanWedstrijd.isPresent()) {
                WedstrijdListDTO wedstrijdListDTO = new WedstrijdListDTO(
                        teamsVanWedstrijd.get().get(0).getTeam(),
                        teamsVanWedstrijd.get().get(0).getScore(),
                        teamsVanWedstrijd.get().get(1).getTeam(),
                        teamsVanWedstrijd.get().get(1).getScore(),
                        wedstrijd.getTijdstip(),
                        wedstrijd.getId());
                frontendList.add(wedstrijdListDTO);
            }
        }
        List<WedstrijdListDTO> gesorteerdeList =  frontendList.stream().sorted(Comparator.comparing(WedstrijdListDTO::getTijdstip,Comparator.reverseOrder()))
                .collect(Collectors.toList());

        return Optional.of(gesorteerdeList);
    }


    /**
     * if statements gaan er voor zorgen dat die niet 2 keer gemaakt worden
     * @param team in welke team de actieve spelers gezocht moeten worden en waarvan die actieve spelers de 2 team_wedstrijden toegevoegd moet worden.
     * @param team_wedstrijd1 eerste team_wedstrijd
     * @param team_wedstrijd2 tweede team_wedstrijd
     */
    public void addWedstrijdenAanPersoonlijkHistoriekVanSpeler(Team team,Team_wedstrijd team_wedstrijd1,Team_wedstrijd team_wedstrijd2){
        for (Speler_team speler_team : spelerTeamDAO.findSpeler_teamsByTeamAndIsreserve(team,false).get()){
            if (persoonlijkMatchHistoriekDAO.findPersoonlijkMatchHistoriekBySpelerAndTeamWedstrijd(speler_team.getSpeler(),team_wedstrijd1).isEmpty()){
                PersoonlijkMatchHistoriek persoonlijkMatchHistoriek = new PersoonlijkMatchHistoriek.Builder()
                        .speler(speler_team.getSpeler()).team_wedstrijd(team_wedstrijd1).build();
                persoonlijkMatchHistoriekDAO.save(persoonlijkMatchHistoriek);
            }
            if (persoonlijkMatchHistoriekDAO.findPersoonlijkMatchHistoriekBySpelerAndTeamWedstrijd(speler_team.getSpeler(),team_wedstrijd2).isEmpty()) {
                PersoonlijkMatchHistoriek persoonlijkMatchHistoriek1 = new PersoonlijkMatchHistoriek.Builder()
                        .speler(speler_team.getSpeler()).team_wedstrijd(team_wedstrijd2).build();
                persoonlijkMatchHistoriekDAO.save(persoonlijkMatchHistoriek1);
            }

        }
    }

    /**
     * @param id waarmee naar een wedstrijd wordt gezocht
     * @return een wedstrijdListDTO dat één volledige wedstrijd voorstelt
     */
    public Optional<WedstrijdListDTO> getWedstrijdenById(Long id){

        Wedstrijd wedstrijd = wedstrijdDao.getById(id);
        List<Team_wedstrijd> teamsVanWedstrijd = teamWedstrijdDAO.findTeam_wedstrijdsByWedstrijd(wedstrijd).get();

        WedstrijdListDTO dto = new WedstrijdListDTO(
                teamsVanWedstrijd.get(0).getTeam(),
                teamsVanWedstrijd.get(0).getScore(),
                teamsVanWedstrijd.get(1).getTeam(),
                teamsVanWedstrijd.get(1).getScore(),
                wedstrijd.getTijdstip(),
                wedstrijd.getId()
        );

        return Optional.of(dto);
    }

    /**
     * @param id van de team waarnaar gezocht wordt
     * @param speler speler die gecontrolleerd moet worden of die in in team zit
     * @return true als die niet in het team zit false als die er wel in zit
     */
    public boolean spelerZitInTeam(long id, Speler speler){
        Optional<Team> team = teamDao.findById(id);
        if (team.isPresent()){
            Optional<Speler_team> speler_team = spelerTeamDAO.findSpeler_teamBySpelerAndTeam(speler,team.get());
            if (speler_team.isEmpty()){
                return true;
            }
        }
        return false;
    }

}
