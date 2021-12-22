package com.example.backend.controller;

import com.example.backend.DTO.SpelerDTO;
import com.example.backend.config.SpelerPrincipal;
import com.example.backend.model.Speler;
import com.example.backend.model.Speler_team;
import com.example.backend.service.ManagerService;
import com.example.backend.service.SpelerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.FORBIDDEN;


@RestController
@RequestMapping("/spelers")
public class SpelerController {
    private final SpelerService service;
    private final ManagerService managerService;

    @Autowired
    public SpelerController(SpelerService service, ManagerService managerService) {
        this.service = service;
        this.managerService = managerService;
    }

    @ApiOperation("Create een speler")
    @PostMapping
    public ResponseEntity spelerToevoegen(
            @RequestBody SpelerDTO spelerDTO){
        if (managerService.usernameBestaad(spelerDTO.getUsername())){
            return new ResponseEntity("Gebruikersnaam bestaat al",HttpStatus.BAD_REQUEST);
        }
         service.createSpeler(spelerDTO);

        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation("Update een speler")
    @Transactional
    @PutMapping( "/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public ResponseEntity updateSpeler(
            @ApiParam("Id van de speler")
            @PathVariable("id") long id,@RequestBody SpelerDTO spelerDTO){
        try {
            service.update(id, spelerDTO);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity(e, FORBIDDEN);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @ApiOperation("Geeft een lijst van alle spelers")
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<List<Speler>> getSpelers(){
        return service.getAllSpelers();
    }

    @ApiOperation("Geeft een lijst van alle speler die niet in een specifiek team zitten")
    @GetMapping("/team/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<List<Speler>> getSpelersNietInTeam(
            @ApiParam("Id van het team")
            @PathVariable("id") long team_id){
        return service.getSpelersNietInTeam(team_id);
    }

    @ApiOperation("Geeft een speler")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<Speler> getSpelerById(
            @ApiParam("Id van de speler")
            @PathVariable("id")long id){
        try {
            SpelerPrincipal principal= (SpelerPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Speler ingelogdeSpeler = principal.getSpeler();
            if (!ingelogdeSpeler.getId().equals(id)){
                return Optional.of(ingelogdeSpeler);
            }else{
                return service.getById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @ApiOperation("Geeft een speler via naam voornaam")
    @GetMapping("/getby")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<Speler> getSpelerByVoornaamAndNaam(
            @ApiParam("Naam van de speler")
            @RequestParam String naam,
            @ApiParam("Voornaam van de speler")
            @RequestParam String voornaam){
        return service.getSpelerByVoornaamAndNaam(voornaam, naam);
    }

    /**
     * @param team_id ID van team waar de spelers zullen worden opgehaald
     * @return list van speler_team waar de team de team_id bevat en de isreserve op false staat
     */
    @ApiOperation("Geeft een lijst van alle actieve spelers van een team")
    @GetMapping("/actief/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<List<Speler_team>> getActieveSpelersVanTeam(
            @ApiParam("Id van het team")
            @PathVariable("id") long team_id){
        return service.getActieveSpelersVanEenTeam(team_id);

    }

    /**
     * @param team_id ID van team waar de spelers zullen worden opgehaald
     * @return list van speler_team waar de team de team_id bevat en de isreserve op true staat
     */
    @ApiOperation("Geeft een lijst van alle reserve spelers van een team")
    @GetMapping("/reserve/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','SPELER')")
    public Optional<List<Speler_team>> getReserveSpelersVanTeam(
            @ApiParam("ID van het team")
            @PathVariable("id") long team_id){
        return service.getReserveSpelersVanEenTeam(team_id);
    }
}
