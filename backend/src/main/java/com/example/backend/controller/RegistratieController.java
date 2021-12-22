package com.example.backend.controller;


import com.example.backend.DTO.RegistratieDTO;
import com.example.backend.config.ManagerPrincipal;
import com.example.backend.config.SpelerPrincipal;
import com.example.backend.model.Manager;
import com.example.backend.model.Speler_team;
import com.example.backend.repo.SpelerTeamDAO;
import com.example.backend.service.ManagerService;
import com.example.backend.service.RegistratieService;
import com.example.backend.service.SpelerService;
import com.example.backend.service.TeamService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


@RestController
@RequestMapping("/registraties")
public class RegistratieController {

    private final RegistratieService registratieService;
    private final ManagerService managerService;
    private final TeamService teamService;
    private final SpelerService spelerService;

    @Autowired
    public RegistratieController(RegistratieService registratieService, SpelerTeamDAO spelerTeamDAO, ManagerService managerService, TeamService teamService, SpelerService spelerService) {
        this.registratieService = registratieService;
        this.managerService = managerService;
        this.teamService = teamService;
        this.spelerService = spelerService;
    }

    @ApiOperation("Geeft een lijst van alle registraties van een team")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<List<Speler_team>> getRegistratiesTeam(
            @ApiParam("Het id van het gewenste team")
            @PathVariable("id") long id){
        return registratieService.getRegistratiesTeam(id);

    }

    @ApiOperation("Haalt een speler-team registratie")
    @GetMapping("/kick/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<Speler_team> geSpelerTeamById(
            @ApiParam("Speler-team id")
            @PathVariable("id") long id){
        return  registratieService.getSpelerTeamById(id);
    }



    @ApiOperation("Een speler uit een team verwijderen")
    @Transactional
    @DeleteMapping("/delete/{spelerteamid}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteSpelerFromTeam(
            @ApiParam("Speler-team id")
            @PathVariable
                    Long spelerteamid)
    {
        return registratieService.deleteSpelerFromTeam(spelerteamid);
    }

    @ApiOperation("Zet een speler in een reserveteam")
    @PostMapping("/reserve")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> reserveTeamToewijzing(
            @ApiParam("Id van de speler")
          @RequestBody RegistratieDTO registratieDTO) {

        try {
            Manager manager = managerService.getIngelogdeManager();
            if (teamService.teamHoortBijManager(manager,registratieDTO.getTeam_id())){
                return registratieService.reserveTeamToewijzing(registratieDTO);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Je kan geen spelers toevoegen bij iemand anders zijn team" + e.getMessage(), HttpStatus.FORBIDDEN);
        }
       return new ResponseEntity("Er iets fout gegaan",HttpStatus.CONFLICT);

    }

    @ApiOperation("Zet een speler in het actieve team")
    @Transactional
    @PutMapping("/promote/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> promoteToActive(
            @ApiParam("Id van het speler_team")
            @PathVariable Long id){
        return registratieService.promoteToActive(id);
    }

    @ApiOperation("Zet een speler in het reserve team")
    @Transactional
    @PutMapping("/demote/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> demoteToReserve(
            @ApiParam("Id van het speler_team")
            @PathVariable Long id){
        return registratieService.demoteToReserve(id);
    }

    @GetMapping("/auth")
    public int getRoleIngelogdeSpeler(){
        try {
            SpelerPrincipal principal = (SpelerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal != null){
                return 1;
            }
        } catch (Exception e) {
            try {
                ManagerPrincipal principal1 =(ManagerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal1 != null){
                    return 2;
                }
            }catch (Exception ex){
                return 0;
            }
        }
        return 0;
    }
}
