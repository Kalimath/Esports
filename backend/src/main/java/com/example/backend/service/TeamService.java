package com.example.backend.service;

import com.example.backend.DTO.TeamStatistiekDTO;
import com.example.backend.DTO.WedstrijdListDTO;
import com.example.backend.model.Manager;
import com.example.backend.model.Team;
import com.example.backend.repo.ManagerDAO;
import com.example.backend.repo.TeamDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {
    private final TeamDao teamDao;
    private final ManagerDAO managerDAO;
    private final WedstrijdService wedstrijdService;
    private final ManagerService managerService;

    @Autowired
    public TeamService(TeamDao teamDao, ManagerDAO managerDAO, WedstrijdService wedstrijdService, ManagerService managerService) {
        this.teamDao = teamDao;
        this.managerDAO = managerDAO;
        this.wedstrijdService = wedstrijdService;
        this.managerService = managerService;
    }

    /**
     * @param id waarmee naar een team wordt gezocht
     * @param naam die aangepast moet worden
     * @return veranderde team
     */
    public Optional<Team> wijzigingNaam(long id , String naam){
        Optional<Team> teamGevonden = teamDao.findById(id);
        if (teamGevonden.isPresent()){
            teamGevonden.get().setNaam(naam);
        }
        return teamGevonden;
    }

    /**
     * @param manager om alle teams van hem op te halen
     * @param id team id waarmee gecontrolleerd wordt
     * @return true indien dit team in de lijst van teams van de manager zit false indien dit niet zo is
     */
    public boolean teamHoortBijManager(Manager manager , long id){
        List<Team> teams = teamDao.findAllByManager(manager).get();
        Optional<Team> team = teamDao.findById(id);

        return teams.contains(team.get());
    }

    /**
     * @param id waarmee naar een team gezocht wordt
     * @return een optional team
     */
    public Optional<Team> getById(long id){
        return teamDao.findById(id);
    }

    /**
     * @param team die aangemaakt moet worden
     * @param id van de manager waar dit team aan gekoppeld moet worden
     * @return de aangemaakte team
     */
    public Team createTeam(Team team,long id) {
          Team team1 =  teamDao.save(team);
          Optional<Manager> manager = managerDAO.findById(id);
          if (manager.isPresent()){
              managerService.updateTeamManager(id,team1.getId());
          }
          return team1;

    }

    /**
     * @return geeft alle teams terug
     */
    public Optional<List<Team>> getAllTeams(){
        return Optional.of(teamDao.findAll());
    }

    /**
     * @param id van de manager
     * @return alle teams van de manager
     */
    public Optional<List<Team>> getAllTeamsByManager(long id){
        Optional<Manager> manager = managerDAO.findById(id);
        if (manager.isPresent()){
            return teamDao.findAllByManager(manager.get());
        }
        return  Optional.of(new ArrayList<>());

    }

    /**
     * @param naam waarme naar een team gezocht wordt
     * @return de team die deze naam heeft of een optional.emty indien niet gevonden
     */
    public Optional<Team> getTeamByNaam(String naam){
        return teamDao.findFirstByNaam(naam);
    }

    /**
     * @param teamId id van de team waar de statistiek van moet worden opgehaald worden
     * @return teamstatistiek dto met de nodige informatie
     */
    public Optional<TeamStatistiekDTO> getTeamStatistiek(long teamId){

        List<WedstrijdListDTO> wedstrijden = wedstrijdService.getMatchHistoriekTeam(teamId).get();
        int wins = 0;
        int losses = 0;
        for (WedstrijdListDTO w:wedstrijden) {
            if (w.getTeamA().getId() == teamId){
                if(w.getScoreteama()>w.getScoreteamb()){
                    wins++;
                }else if (w.getScoreteama()<w.getScoreteamb()){
                    losses++;
                }
            }else if (w.getTeamB().getId() == teamId){
                if(w.getScoreteamb()>w.getScoreteama()){
                    wins++;
                }else if (w.getScoreteamb()<w.getScoreteama()){
                    losses++;
                }
            }
        }
        TeamStatistiekDTO statistiekDTO = new TeamStatistiekDTO.Builder()
                .wins(wins)
                .losses(losses)
                .wn8(calculateWn8(wins, wedstrijden.size()))
                .build();

        return Optional.of(statistiekDTO);
    }

    /**
     * @param wins hoevaak gewonnen
     * @param matches hoeveel matches
     * @return de winrate
     */
    private double calculateWn8(int wins, int matches){

        return Math.round((double)wins/(double) matches*100);
    }

}
