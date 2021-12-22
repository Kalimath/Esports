package com.example.backend.controller;

import com.example.backend.DTO.ScoreDTO;
import com.example.backend.DTO.WedstrijdDTO;
import com.example.backend.DTO.WedstrijdListDTO;
import com.example.backend.exceptions.TeamNotFound;
import com.example.backend.model.Manager;
import com.example.backend.model.Speler;
import com.example.backend.model.Wedstrijd;
import com.example.backend.service.ManagerService;
import com.example.backend.service.SpelerService;
import com.example.backend.service.TeamService;
import com.example.backend.service.WedstrijdService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/wedstrijden")
@RestController
public class WedstrijdController {
    private final WedstrijdService service;
    private final SpelerService spelerService;
    private final ManagerService managerService;
    private final TeamService teamService;

    @Autowired
    public WedstrijdController(WedstrijdService service, SpelerService spelerService, ManagerService managerService, TeamService teamService) {
        this.service = service;
        this.spelerService = spelerService;
        this.managerService = managerService;
        this.teamService = teamService;
    }

    @ApiOperation("Maakt een nieuwe wedstrijd")
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public WedstrijdDTO wedstrijdToevoegen(@RequestBody WedstrijdDTO wedstrijdDTO){
        try {
            WedstrijdDTO result = service.createWedstrijd(wedstrijdDTO);

            return result;
        } catch (TeamNotFound e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }



    @ApiOperation("Wijzigt de score van de teams van een wedstrijd")
    @Transactional
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity wedstrijdScoreToevoegenVoorTeam(
            @ApiParam("Id van de wedstrijd")
            @PathVariable("id")long id, @RequestBody ScoreDTO scoreDTO){
            if(service.setWedstrijdscoreVoorTeam(id, scoreDTO)){
                return new ResponseEntity(HttpStatus.OK);
            }else {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
    }

    @ApiOperation("Geeft een lijst van alle wedstrijden")
    @GetMapping("/dto")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<List<WedstrijdListDTO>> getAlleWedstrijden(){
        return service.getWedstrijden();
    }

    @ApiOperation("Geeft de score van een wedstrijd")
    @GetMapping("/scoreDTO/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<WedstrijdListDTO> getScoreDtoById(
            @ApiParam("Id van de wedstrijd")
            @PathVariable Long id){
        return service.getWedstrijdenById(id);
    }

    @ApiOperation("Geeft een lijst van alle wedstrijden van een team")
    @GetMapping("/team/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional <List<WedstrijdListDTO>> getWedstrijdenByTeam(
            @ApiParam("Id van het team")
            @PathVariable("id") long id){
        try {
            Optional<Speler> speler = spelerService.getIngelogdeSpeler();
            if (speler.isPresent()){
                if (!service.spelerZitInTeam(id,speler.get())){
                    return service.getMatchHistoriekTeam(id);
                }
            }
        } catch (Exception e) {
            try {
                Manager manager = managerService.getIngelogdeManager();
                if (manager != null){
                    if (teamService.teamHoortBijManager(manager,id)){
                        return service.getMatchHistoriekTeam(id);
                    }
                }
            }catch (Exception ex){
                return Optional.of(new ArrayList<>());
            }
        }
        return Optional.of(new ArrayList<>());

    }

    @ApiOperation("Geeft een lijst van alle wedstrijden van een speler")
    @GetMapping("/speler/{id}")
    @PreAuthorize("hasRole('SPELER')")
    public Optional <List<WedstrijdListDTO>> getWedstrijdenBySpeler(
            @ApiParam("Id van de speler")
            @PathVariable("id") long id){
        Optional<Speler> speler = spelerService.getIngelogdeSpeler();
        if (speler.isPresent()){
            return service.getPersoonlijkMatchHistoriek(speler.get().getId());
        }
        return service.getPersoonlijkMatchHistoriek(id);
    }

}
