package com.example.backend.controller;

import com.example.backend.DTO.TeamDTO;
import com.example.backend.DTO.TeamStatistiekDTO;
import com.example.backend.model.Manager;
import com.example.backend.model.Team;
import com.example.backend.service.ManagerService;
import com.example.backend.service.TeamService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;


@RequestMapping("teams")
@RestController
public class TeamController {

    private final TeamService service;
    private final ManagerService managerService;

    @Autowired
    public TeamController(TeamService service, ManagerService managerService) {
        this.service = service;
        this.managerService = managerService;
    }

    @ApiOperation("Wijzigt de naam van een team")
    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Team> weizigingNaam(@PathVariable("id") long id, @RequestBody TeamDTO teamDTO){

        if (service.getById(id).isEmpty()){
            return new ResponseEntity("Niet gevonden met id" + id, HttpStatus.NOT_FOUND);
        }

        try {
            Manager manager = managerService.getIngelogdeManager();
            if (service.teamHoortBijManager(manager,id)){
                return new ResponseEntity(service.wijzigingNaam(id, teamDTO.getNaam()),HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity("Dit team hoort niet tot bij u teams",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity("Er is een onverwacte fout verkomen", HttpStatus.CONFLICT);


    }

    @ApiOperation("Geeft een team")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public ResponseEntity<Team> getById(
            @ApiParam("Id van het team")
            @PathVariable("id") long id){
        try {


                return new ResponseEntity( service.getById(id),HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity("Team hoort niet bij ingelogde manager", HttpStatus.FORBIDDEN);
        }

    }

    @ApiOperation("Geeft een team")
    @GetMapping("/getBy/")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<Team> getByNaam(
            @ApiParam("Naam van het team")
            @RequestParam String naam){
        return service.getTeamByNaam(naam);
    }

    @ApiOperation("Geeft een lijst van alle teams")
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<List<Team>> getTeams(){
        return service.getAllTeams();
    }

    @ApiOperation("Geeft een lijst van de manager zijn teams")
    @GetMapping("/manager/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public Optional<List<Team>> getTeamsByManager(
            @ApiParam("Id van de manager")
            @PathVariable("id") long id){

       Manager manager = managerService.getIngelogdeManager();
        if (manager != null){
            return service.getAllTeamsByManager(manager.getId());
        }
        return service.getAllTeamsByManager(id);

    }

    @ApiOperation("Maakt een nieuw team aan")
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public Team createTeam(@RequestBody Team team){

        Manager manager = managerService.getIngelogdeManager();
        return service.createTeam(team,manager.getId());
    }

    @ApiOperation("Haalt de statistieken op van een team")
    @GetMapping("/statistiek/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<TeamStatistiekDTO> getStatistiek(
            @ApiParam("Id van het team")
            @PathVariable("id") long id){
        return service.getTeamStatistiek(id);
    }

    @GetMapping("/ismijn/team/{id}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    public boolean isTeamVanIngelogdeManager(@PathVariable("id") long id){
        try {
            Manager manager = managerService.getIngelogdeManager();
            if (service.teamHoortBijManager(manager,id)){
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;

    }

}
